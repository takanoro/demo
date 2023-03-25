package xyz.btc.demo.validator

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.btc.demo.model.GetTransfersQuery
import java.time.ZonedDateTime


@ExtendWith(MockKExtension::class)
internal class DateRangeValidatorTest {

    @MockK
    private lateinit var context: ConstraintValidatorContext

    @MockK
    private lateinit var validator: DateRangeValidator

    @MockK
    private lateinit var builder: ConstraintValidatorContext.ConstraintViolationBuilder


    @BeforeEach
    fun setUp() {
        every {
            validator.initialize(any())
        } answers { callOriginal() }

        every {
            validator.isValid(any(), context)
        } answers { callOriginal() }

        val testClass = DateConstraint()

        validator.initialize(testClass)
    }

    @ParameterizedTest
    @MethodSource("dateRangeValidatorData")
    fun testDateRangeValidator(
        startDatetime: ZonedDateTime?,
        endDateTime: ZonedDateTime?,
        expectedMessages: List<String>
    ) {
        val query = GetTransfersQuery(
            startDatetime = startDatetime,
            endDatetime = endDateTime
        )

        val messages = mutableListOf<String>()

        justRun { context.disableDefaultConstraintViolation() }

        every { builder.addConstraintViolation() } returns context
        every { context.buildConstraintViolationWithTemplate(capture(messages)) } returns builder

        assertEquals(false, validator.isValid(query, context))

        assertEquals(expectedMessages, messages)
    }

    companion object {

        @JvmStatic
        fun dateRangeValidatorData() =
            listOf(
                Arguments.of(
                    null, null,
                    listOf("[startDatetime] must not be null.", "[endDatetime] must not be null.")
                ),
                Arguments.of(
                    ZonedDateTime.parse("2022-03-14T23:22:12+03:00"),
                    ZonedDateTime.parse("2022-03-14T20:22:12+02:00"),
                    listOf("[startDatetime] is greater than [endDatetime].")
                ),
                Arguments.of(
                    ZonedDateTime.parse("2001-03-14T23:22:12+03:00"),
                    ZonedDateTime.parse("2001-03-14T20:22:12+02:00"),
                    listOf("[startDatetime] is greater than [endDatetime].")
                ),
                Arguments.of(
                    ZonedDateTime.parse("2001-03-14T23:22:12+03:00"),
                    ZonedDateTime.parse("2001-03-15T20:22:12+02:00"),
                    listOf("[starDatetime] or [endDateTime] must be greater than [January 03, 2009].")
                ),
                Arguments.of(
                    ZonedDateTime.parse("2001-03-14T16:22:12-02:00"),
                    ZonedDateTime.parse("2001-03-14T18:22:12+04:00"),
                    listOf("[startDatetime] is greater than [endDatetime].")
                ),
                Arguments.of(
                    ZonedDateTime.now().plusDays(2),
                    ZonedDateTime.now().plusDays(2),
                    listOf("[starDatetime] or [endDateTime] must be lower than `current datetime + 10 minutes`.")
                )
            )
    }

}