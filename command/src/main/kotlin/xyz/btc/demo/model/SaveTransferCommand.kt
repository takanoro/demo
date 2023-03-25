package xyz.btc.demo.model

import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.validation.annotation.Validated
import xyz.btc.demo.validator.DateConstraint
import java.math.BigDecimal
import java.time.ZonedDateTime

@Validated
data class SaveTransferCommand(
    @field:NotNull
    @field:Positive
    @field:Digits(integer = 16, fraction = 8)
    val amount: BigDecimal?,
    @field:DateConstraint
    val datetime: ZonedDateTime?
)

