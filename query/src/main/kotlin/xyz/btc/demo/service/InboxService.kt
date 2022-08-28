package xyz.btc.demo.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.btc.demo.configuration.properties.InboxProperties
import xyz.btc.demo.entity.InboxEntity
import xyz.btc.demo.repository.InboxRepository
import java.time.Instant

@Service
class InboxService(
    private val inboxRepository: InboxRepository,
    private val inboxProperties: InboxProperties
) {

    private val logger = LoggerFactory.getLogger(InboxService::class.java)

    @Transactional
    suspend fun receiveEvent(eventId: String, message: String, aggregate: String) {
        inboxRepository.upsert(
            InboxEntity(
                id = eventId,
                aggregate = aggregate,
                message = message,
                createdAt = Instant.now()
            )
        )
    }

    @Transactional
    suspend fun processEvents(aggregate: String, handler: suspend (List<String>) -> Unit) {
        val batchSize = inboxProperties.aggregate[aggregate]!!.batchSize

        val inboxEntries = inboxRepository.findByAggregate(aggregate, batchSize)

        if (inboxEntries.isEmpty()) return

        val ids = inboxEntries.map(InboxEntity::id)
        val inboxMessages = inboxEntries.map(InboxEntity::message)

        handler(inboxMessages)

        inboxRepository.updateProcessed(ids)
    }

    @Transactional
    suspend fun deleteProceeded() {
        logger.debug("Removing processed events from inbox.")

        inboxRepository.deleteProcessed()
    }

}
