package xyz.btc.demo.mapper

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.btc.demo.model.SaveTransferCommand
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

internal class TransferMapperTest {

    private val mapper = TransferMapper()

    @Test
    fun testMapper() = runBlocking {
        val amount = BigDecimal.valueOf(10.11)
        val zoneId = ZoneId.of("+03:00")
        val datetime = ZonedDateTime.of(2022, 9, 11, 19, 22, 11, 1, zoneId)
        val command = SaveTransferCommand(amount, datetime)

        val entity = mapper.map(command)

        assertEquals(amount, entity.amount, "amounts are not equals")
        assertEquals(zoneId.toString(), entity.timezone, "timezones are not equals")
        assertEquals(
            datetime.withZoneSameInstant(ZoneId.of("Z")).toLocalDateTime(),
            entity.datetime,
            "dateTimes are not equals"
        )
    }

}