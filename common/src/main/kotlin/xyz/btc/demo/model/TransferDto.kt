package xyz.btc.demo.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferDto(
    val id: String,
    val amount: BigDecimal,
    val datetime: LocalDateTime,
    val timezone: String
)