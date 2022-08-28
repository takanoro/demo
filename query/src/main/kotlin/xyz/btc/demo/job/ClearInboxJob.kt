package xyz.btc.demo.job

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import xyz.btc.demo.service.InboxService

@Component
class ClearInboxJob(
    private val inboxService: InboxService
) {

    @Scheduled(cron = "0/20 * * * * *")
    fun send() {
        runBlocking {
            inboxService.deleteProceeded()
        }
    }

}