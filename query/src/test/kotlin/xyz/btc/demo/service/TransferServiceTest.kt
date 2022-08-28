package xyz.btc.demo.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.entity.TransferViewEntity
import xyz.btc.demo.repository.TransferViewRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZonedDateTime

@ExtendWith(MockKExtension::class)
internal class TransferServiceTest {

    @MockK
    private lateinit var transferViewRepository: TransferViewRepository

    private lateinit var transferService: TransferService

    @BeforeEach
    fun init() {
        transferService = TransferService(transferViewRepository)
    }

    @ParameterizedTest
    @MethodSource("updateAmountsData")
    fun testUpdateAmounts(
        inputData: List<TransferEntity>,
        expectedList: List<TransferViewEntity>
    ) = runBlocking {
        coEvery { transferViewRepository.upsertAll(entities = any()) } returns emptyList()

        transferService.updateAmounts(inputData)

        coVerify { transferViewRepository.upsertAll(expectedList) }
    }


    @ParameterizedTest
    @MethodSource("calculateAmountForRangeData")
    fun testCalculateAmountForRange(
        start: ZonedDateTime,
        end: ZonedDateTime,
        normalizedStart: LocalDateTime,
        normalizedEnd: LocalDateTime
    ) = runBlocking {
        coEvery { transferViewRepository.findAll(any(), any()) } returns emptyList()

        transferService.calculateAmountForRange(start, end)

        coVerify { transferViewRepository.findAll(normalizedStart, normalizedEnd) }
    }

    companion object {
        @JvmStatic
        fun updateAmountsData() =
            listOf(
                Arguments.of(
                    listOf(
                        TransferEntity(
                            "id0",
                            BigDecimal.valueOf(15.7),
                            LocalDateTime.parse("2022-03-14T16:22:12"),
                            "+00:00"
                        ),
                        TransferEntity(
                            "id1",
                            BigDecimal.valueOf(1.11),
                            LocalDateTime.parse("2022-03-14T17:44:11"),
                            "+00:00"
                        ),
                        TransferEntity(
                            "id2",
                            BigDecimal.valueOf(2.22),
                            LocalDateTime.parse("2022-03-14T17:33:15"),
                            "+00:00"
                        )
                    ),
                    listOf(
                        TransferViewEntity(BigDecimal.valueOf(15.7), LocalDateTime.parse("2022-03-14T16:00:00")),
                        TransferViewEntity(BigDecimal.valueOf(3.33), LocalDateTime.parse("2022-03-14T17:00:00"))
                    )
                ),
                Arguments.of(
                    listOf(
                        TransferEntity(
                            "id0",
                            BigDecimal.valueOf(1.11),
                            LocalDateTime.parse("2022-03-14T17:44:11"),
                            "+00:00"
                        ),
                        TransferEntity(
                            "id1",
                            BigDecimal.valueOf(2.22),
                            LocalDateTime.parse("2022-03-14T17:33:15"),
                            "+00:00"
                        ),
                        TransferEntity(
                            "id2",
                            BigDecimal.valueOf(3.33),
                            LocalDateTime.parse("2022-03-14T17:11:55"),
                            "+00:00"
                        ),
                    ),
                    listOf(
                        TransferViewEntity(BigDecimal.valueOf(6.66), LocalDateTime.parse("2022-03-14T17:00:00"))
                    )
                )
            )


        @JvmStatic
        fun calculateAmountForRangeData() =
            listOf(
                Arguments.of(
                    ZonedDateTime.parse("2022-03-14T16:22:12+03:00"),
                    ZonedDateTime.parse("2022-03-14T20:22:12+02:00"),
                    LocalDateTime.parse("2022-03-14T13:00:00"),
                    LocalDateTime.parse("2022-03-14T18:00:00"),
                ),
                Arguments.of(
                    ZonedDateTime.parse("2022-03-14T16:22:12+07:00"),
                    ZonedDateTime.parse("2022-03-14T18:21:12-02:00"),
                    LocalDateTime.parse("2022-03-14T09:00:00"),
                    LocalDateTime.parse("2022-03-14T20:00:00"),
                )
            )
    }


}