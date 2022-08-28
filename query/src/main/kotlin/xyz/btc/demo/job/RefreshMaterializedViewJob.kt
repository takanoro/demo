package xyz.btc.demo.job

import kotlinx.coroutines.runBlocking
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import xyz.btc.demo.service.TransferService

@Component
class RefreshMaterializedViewJob(
    private val transferService: TransferService
) {

    @Scheduled(cron = "0/10 * * * * *")
    @SchedulerLock(name = "refresh_materialized_view_job", lockAtLeastFor = "1m")
    fun send() {
        runBlocking {
            transferService.refreshView()
        }
    }

}