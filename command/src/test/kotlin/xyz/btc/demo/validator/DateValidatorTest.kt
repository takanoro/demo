package xyz.btc.demo.validator


import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.ZonedDateTime
import javax.validation.ConstraintValidatorContext
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder

@ExtendWith(MockKExtension::class)
internal class DateValidatorTest {

    @MockK
    private lateinit var context: ConstraintValidatorContext

    @MockK
    private lateinit var validator: DateValidator

    @MockK
    private lateinit var builder: ConstraintViolationBuilder

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
        datetime: ZonedDateTime?,
        expectedMessages: List<String>
    ) {
        val messages = mutableListOf<String>()

        justRun { context.disableDefaultConstraintViolation() }

        every { builder.addConstraintViolation() } returns context
        every { context.buildConstraintViolationWithTemplate(capture(messages)) } returns builder

        assertEquals(false, validator.isValid(datetime, context))

        assertEquals(expectedMessages, messages)
    }

    companion object {

        @JvmStatic
        fun dateRangeValidatorData() =
            listOf(
                Arguments.of(
                    null,
                    listOf("[datetime] must not be null.")
                ),
                Arguments.of(
                    ZonedDateTime.now().plusDays(10),
                    listOf("[datetime] must be lower than `current time + 10 minutes`.")
                ),
                Arguments.of(
                    ZonedDateTime.parse("2001-03-14T23:22:12+03:00"),
                    listOf("[datetime] must be greater than [January 03, 2009].")
                ),
            )
    }

}