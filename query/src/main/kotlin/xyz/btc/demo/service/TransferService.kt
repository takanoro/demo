package xyz.btc.demo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.entity.TransferViewEntity
import xyz.btc.demo.repository.TransferViewRepository
import xyz.btc.demo.util.Consts.UTC_ZONE_ID
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

@Service
class TransferService(
    private val transferViewRepository: TransferViewRepository
) {

    @Transactional
    suspend fun updateAmounts(transferEntities: List<TransferEntity>) {
        val views = transferEntities.map { transfer ->
            val roundedDatetime = transfer.datetime.with(LocalTime.of(transfer.datetime.hour, 0, 0))
            transfer.copy(datetime = roundedDatetime)
        }.groupBy(TransferEntity::datetime, TransferEntity::amount)
            .map { (datetime, amounts) ->
                TransferViewEntity(amounts.reduce(BigDecimal::plus), datetime)
            }

        transferViewRepository.upsertAll(views)
    }

    @Transactional
    suspend fun refreshView() =
        transferViewRepository.refreshMaterializedView()

    @Transactional(readOnly = true)
    suspend fun calculateAmountForRange(
        startDatetime: ZonedDateTime,
        endDatetime: ZonedDateTime
    ): List<TransferViewEntity> =
        transferViewRepository.findAll(
            convertAndNormalize(startDatetime),
            convertAndNormalize(endDatetime)
        )

    private fun convertAndNormalize(datetime: ZonedDateTime): LocalDateTime =
        datetime.withZoneSameInstant(UTC_ZONE_ID).toLocalDateTime()
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

}