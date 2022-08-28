package xyz.btc.demo.job

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import xyz.btc.demo.service.OutboxService

@Component
class OutboxSenderJob(
    private val outboxService: OutboxService
) {

    @Scheduled(cron = "0/1 * * * * *")
    fun send() {
        runBlocking {

            outboxService.sendEvents("TRANSFER")
        }
    }

}