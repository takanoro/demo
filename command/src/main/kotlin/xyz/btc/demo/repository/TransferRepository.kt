package xyz.btc.demo.repository

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitOne
import org.springframework.stereotype.Repository
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.util.getK


@Repository
class TransferRepository(
    private val client: DatabaseClient
) {

    private companion object {
        const val INSERT_ALL_QUERY =
            "insert into transfer(amount, datetime, timezone) values (:amount, :datetime, :timezone) returning id"
    }

    suspend fun insert(entity: TransferEntity): TransferEntity =
        client.sql(INSERT_ALL_QUERY)
            .bind("amount", entity.amount)
            .bind("datetime", entity.datetime)
            .bind("timezone", entity.timezone)
            .map { row -> entity.copy(id = row.getK("id")) }
            .awaitOne()

}