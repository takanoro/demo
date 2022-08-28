package xyz.btc.demo.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferEntity(
    val id: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val amount: BigDecimal,
    val datetime: LocalDateTime,
    val timezone: String
)

