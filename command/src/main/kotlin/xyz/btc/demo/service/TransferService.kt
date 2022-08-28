package xyz.btc.demo.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.btc.demo.entity.TransferEntity
import xyz.btc.demo.repository.TransferRepository

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val outboxService: OutboxService
) {

    private companion object {
        private const val TRANSFER_AGGREGATE = "TRANSFER"
    }

    @Transactional
    suspend fun receiveTransfer(entity: TransferEntity) {
        val savedEntity = transferRepository.insert(entity)

        outboxService.registerEvent(savedEntity, TRANSFER_AGGREGATE)
    }

}
