package xyz.btc.demo.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferViewEntity(
    val amount: BigDecimal,
    val datetime: LocalDateTime,
)
