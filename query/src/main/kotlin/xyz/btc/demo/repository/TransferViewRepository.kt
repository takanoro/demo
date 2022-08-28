package xyz.btc.demo.repository

import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.stereotype.Repository
import xyz.btc.demo.entity.TransferViewEntity
import xyz.btc.demo.util.getK
import java.time.LocalDateTime

@Repository
class TransferViewRepository(
    private val client: DatabaseClient
) {

    private companion object {
        const val UPSERT_QUERY =
            """
                insert into transfer_hour_view (datetime, amount) values (:datetime, :amount) 
                    on conflict(datetime)
                do update set amount = (:amount + transfer_hour_view.amount)
            """

        const val CALL_REFRESH_FUNCTION = "call refresh_transfer_amount_materialized_view()"

        const val FIND_ALL_IN_RANGE_SQL =
            "select amount, datetime from transfer_amount_hour_view where datetime between :startDate and :endDate"

        fun mapEntity(row: Row) =
            TransferViewEntity(
                amount = row.getK("amount"),
                datetime = row.getK("datetime")
            )

    }

    suspend fun upsertAll(entities: List<TransferViewEntity>) =
        entities.map { entity ->
            client.sql(UPSERT_QUERY)
                .bind("datetime", entity.datetime)
                .bind("amount", entity.amount)
                .fetch()
                .awaitRowsUpdated()
        }

    suspend fun refreshMaterializedView() =
        client.sql(CALL_REFRESH_FUNCTION)
            .fetch()
            .awaitRowsUpdated()

    suspend fun findAll(startDate: LocalDateTime, endDate: LocalDateTime) =
        client.sql(FIND_ALL_IN_RANGE_SQL)
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .map(::mapEntity)
            .all()
            .asFlow()
            .toList()

}
