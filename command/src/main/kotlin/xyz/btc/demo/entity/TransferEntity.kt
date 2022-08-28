package xyz.btc.demo.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransferEntity(
    override val id: String? = null,
    val amount: BigDecimal,
    val datetime: LocalDateTime,
    val timezone: String
) : IdHolder

interface IdHolder {
    val id: String?
}