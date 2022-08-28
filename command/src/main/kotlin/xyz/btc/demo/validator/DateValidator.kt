package xyz.btc.demo.validator

import xyz.btc.demo.util.Consts.UTC_ZONE_ID
import java.time.ZonedDateTime
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Constraint(validatedBy = [DateValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateConstraint(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)

class DateValidator : ConstraintValidator<DateConstraint, ZonedDateTime> {

    private companion object {
        val ZERO_BLOCK_DATE: ZonedDateTime = ZonedDateTime.of(2009, 1, 3, 0, 0, 0, 0, UTC_ZONE_ID)
    }

    override fun isValid(
        datetime: ZonedDateTime?,
        ctx: ConstraintValidatorContext
    ): Boolean {
        var valid = true
        ctx.disableDefaultConstraintViolation()

        if (datetime == null) {
            ctx.buildConstraintViolationWithTemplate("[datetime] must not be null.")
                .addConstraintViolation()
            valid = false
        }

        if (valid && datetime!!.isBefore(ZERO_BLOCK_DATE)) {
            ctx.buildConstraintViolationWithTemplate("[datetime] must be greater than [January 03, 2009].")
                .addConstraintViolation()
            valid = false
        }

        if (valid && datetime!!.isAfter(ZonedDateTime.now(UTC_ZONE_ID).plusMinutes(10))) {
            ctx.buildConstraintViolationWithTemplate("[datetime] must be lower than `current time + 10 minutes`.")
                .addConstraintViolation()
            valid = false
        }

        return valid
    }

}
