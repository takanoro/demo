package xyz.btc.demo.mapper

import org.springframework.stereotype.Component
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.model.SaveTransferCommand
import xyz.btc.demo.util.Consts.UTC_ZONE_ID

@Component
class TransferMapper {

    suspend fun map(dto: SaveTransferCommand): TransferEntity {
        val offset = dto.datetime!!.offset
        val datetimeUtc = dto.datetime!!.withZoneSameInstant(UTC_ZONE_ID).toLocalDateTime()

        return TransferEntity(
            amount = dto.amount!!,
            datetime = datetimeUtc!!,
            timezone = offset.id
        )
    }

}