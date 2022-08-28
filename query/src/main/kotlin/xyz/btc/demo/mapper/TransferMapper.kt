package xyz.btc.demo.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.entity.TransferViewEntity
import xyz.btc.demo.model.GetTransfersResult

@Component
class TransferMapper(
    private val objectMapper: ObjectMapper
) {

    suspend fun map(entity: TransferViewEntity): GetTransfersResult =
        GetTransfersResult(
            amount = entity.amount,
            datetime = entity.datetime
        )

    suspend fun map(msg: String): TransferEntity = objectMapper.readValue(msg)

}