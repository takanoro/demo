package xyz.btc.demo.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class GetTransfersResult(
    val datetime: LocalDateTime,
    val amount: BigDecimal
)

