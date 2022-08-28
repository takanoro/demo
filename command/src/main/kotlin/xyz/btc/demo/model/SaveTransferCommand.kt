package xyz.btc.demo.model

import org.springframework.validation.annotation.Validated
import xyz.btc.demo.validator.DateConstraint
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Validated
data class SaveTransferCommand(
    @field:NotNull
    @field:Positive
    @field:Digits(integer = 16, fraction = 8)
    val amount: BigDecimal?,
    @field:DateConstraint
    val datetime: ZonedDateTime?
)

