package xyz.btc.demo.repository

import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.stereotype.Repository
import xyz.btc.demo.entity.OutboxEntity
import xyz.btc.demo.util.getK


@Repository
class OutboxRepository(
    private val client: DatabaseClient
) {

    private companion object {
        const val INSERT_ALL_QUERY =
            "insert into outbox (id, aggregate, message, created_at) values (:id::uuid, :aggregate, :message, :createdAt)"

        const val REMOVE_FROM_OUTBOX_QUERY =
            """
                delete from outbox
                where id in (
                    select id
                    from outbox
                    where aggregate = :aggregate
                    order by created_at
                        for update skip locked
                    limit :limit 
                ) returning id, aggregate, message, created_at
            """

        fun mapEntity(row: Row) =
            OutboxEntity(
                id = row.getK("id"),
                aggregate = row.getK("aggregate"),
                message = row.getK("message"),
                createdAt = row.getK("created_at")
            )

    }

    suspend fun insert(entity: OutboxEntity) =
        client.sql(INSERT_ALL_QUERY)
            .bind("id", entity.id)
            .bind("aggregate", entity.aggregate)
            .bind("message", entity.message)
            .bind("createdAt", entity.createdAt)
            .fetch()
            .awaitRowsUpdated()

    suspend fun findByAggregate(aggregate: String, batchSize: Int) =
        client.sql(REMOVE_FROM_OUTBOX_QUERY)
            .bind("aggregate", aggregate)
            .bind("limit", batchSize)
            .map(::mapEntity)
            .all()
            .asFlow()
            .toList()

}