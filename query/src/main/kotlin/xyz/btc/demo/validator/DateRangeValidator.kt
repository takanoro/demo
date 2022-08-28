package xyz.btc.demo.validator

import xyz.btc.demo.model.GetTransfersQuery
import xyz.btc.demo.util.Consts
import xyz.btc.demo.util.Consts.UTC_ZONE_ID
import java.time.ZonedDateTime
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Constraint(validatedBy = [DateRangeValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateConstraint(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class DateRangeValidator : ConstraintValidator<DateConstraint, GetTransfersQuery> {

    private companion object {
        val ZERO_BLOCK_DATE: ZonedDateTime = ZonedDateTime.of(2009, 1, 3, 0, 0, 0, 0, UTC_ZONE_ID)
    }

    override fun isValid(
        obj: GetTransfersQuery,
        ctx: ConstraintValidatorContext
    ): Boolean {
        var valid = true
        ctx.disableDefaultConstraintViolation()

        val startDatetime = obj.startDatetime
        val endDatetime = obj.endDatetime

        if (startDatetime == null) {
            ctx.buildConstraintViolationWithTemplate("[startDatetime] must not be null.")
                .addConstraintViolation()
            valid = false
        }

        if (endDatetime == null) {
            ctx.buildConstraintViolationWithTemplate("[endDatetime] must not be null.")
                .addConstraintViolation()
            valid = false
        }

        if (valid && startDatetime!!.isAfter(endDatetime)) {
            ctx.buildConstraintViolationWithTemplate("[startDatetime] is greater than [endDatetime].")
                .addConstraintViolation()
            valid = false
        }

        if (valid && (startDatetime!!.isBefore(ZERO_BLOCK_DATE) || endDatetime!!.isBefore(ZERO_BLOCK_DATE))) {
            ctx.buildConstraintViolationWithTemplate("[starDatetime] or [endDateTime] must be greater than [January 03, 2009].")
                .addConstraintViolation()
            valid = false
        }

        val futureDate = ZonedDateTime.now(UTC_ZONE_ID).plusMinutes(10)

        if (valid && (startDatetime!!.isAfter(futureDate) || endDatetime!!.isAfter(futureDate))) {
            ctx.buildConstraintViolationWithTemplate("[starDatetime] or [endDateTime] must be lower than `current datetime + 10 minutes`.")
                .addConstraintViolation()
            valid = false
        }

        return valid
    }
}