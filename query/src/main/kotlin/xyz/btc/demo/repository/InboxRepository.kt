package xyz.btc.demo.repository

import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.stereotype.Repository
import xyz.btc.demo.entity.InboxEntity
import xyz.btc.demo.util.getK
import java.util.*


@Repository
class InboxRepository(
    private val client: DatabaseClient
) {

    private companion object {
        const val UPSERT_ALL_QUERY =
            """
                insert into inbox (id, aggregate, message, created_at) 
                values (:id::uuid, :aggregate, :message, :createdAt) on conflict (id) do nothing
            """

        const val FIND_QUERY =
            """
                select id, aggregate, message, created_at from inbox 
                    where proceed = false and aggregate = :aggregate
                order by created_at
                for update skip locked limit :limit
            """

        const val UPDATE_PROCESSED_QUERY =
            """
                update inbox set proceed = true where id = any(:ids)
            """

        const val DELETE_PROCESSED_QUERY =
            """
                delete from inbox where proceed = true and (now() - inbox.created_at) > interval '5 minutes'
            """

        fun mapEntity(row: Row) =
            InboxEntity(
                id = row.getK("id"),
                aggregate = row.getK("aggregate"),
                message = row.getK("message"),
                createdAt = row.getK("created_at")
            )

    }

    suspend fun upsert(entity: InboxEntity) =
        client.sql(UPSERT_ALL_QUERY)
            .bind("id", entity.id)
            .bind("aggregate", entity.aggregate)
            .bind("message", entity.message)
            .bind("createdAt", entity.createdAt)
            .fetch()
            .awaitRowsUpdated()

    suspend fun updateProcessed(ids: List<String>) =
        client.sql(UPDATE_PROCESSED_QUERY)
            .bind("ids", ids.map(UUID::fromString).toTypedArray())
            .fetch()
            .awaitRowsUpdated()

    suspend fun deleteProcessed() =
        client.sql(DELETE_PROCESSED_QUERY)
            .fetch()
            .awaitRowsUpdated()

    suspend fun findByAggregate(aggregate: String, batchSize: Int) =
        client.sql(FIND_QUERY)
            .bind("aggregate", aggregate)
            .bind("limit", batchSize)
            .map(::mapEntity)
            .all()
            .asFlow()
            .toList()

}