package xyz.btc.demo.model

import org.springframework.validation.annotation.Validated
import xyz.btc.demo.validator.DateConstraint
import java.time.ZonedDateTime

@Validated
@DateConstraint
data class GetTransfersQuery(
    val startDatetime: ZonedDateTime?,
    val endDatetime: ZonedDateTime?
)

