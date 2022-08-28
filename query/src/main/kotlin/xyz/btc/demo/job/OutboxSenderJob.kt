package xyz.btc.demo.job

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import xyz.btc.demo.mapper.TransferMapper
import xyz.btc.demo.service.InboxService
import xyz.btc.demo.service.TransferService

@Component
class OutboxSenderJob(
    private val inboxService: InboxService,
    private val transferService: TransferService,
    private val transferMapper: TransferMapper
) {
    private companion object {
        private const val TRANSFER_AGGREGATE = "TRANSFER"
    }

    @Scheduled(cron = "0/5 * * * * *")
    fun send() {
        runBlocking {
            inboxService.processEvents(TRANSFER_AGGREGATE) { messages ->
                val entities = messages.map { message -> transferMapper.map(message) }

                transferService.updateAmounts(entities)
            }
        }
    }

}