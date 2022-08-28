package xyz.btc.demo.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.btc.demo.configuration.properties.OutboxProperties
import xyz.btc.demo.entity.IdHolder
import xyz.btc.demo.entity.OutboxEntity
import xyz.btc.demo.repository.OutboxRepository
import java.time.Instant

@Service
class OutboxService(
    private val outboxRepository: OutboxRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val outboxProperties: OutboxProperties
) {

    private val logger = LoggerFactory.getLogger(OutboxService::class.java)

    suspend fun registerEvent(source: IdHolder, aggregate: String) {
        logger.info("Registering new event for an aggregate: $aggregate")

        val outboxEntity = OutboxEntity(
            id = source.id!!,
            aggregate = aggregate,
            message = objectMapper.writeValueAsString(source),
            createdAt = Instant.now()
        )

        outboxRepository.insert(outboxEntity)
    }

    @Transactional
    suspend fun sendEvents(aggregate: String) {
        val topic = outboxProperties.aggregate[aggregate]!!.topic
        val batchSize = outboxProperties.aggregate[aggregate]!!.batchSize

        val outboxEvents = outboxRepository.findByAggregate(aggregate, batchSize)

        if (outboxEvents.isEmpty()) return

        logger.info("Processing ${outboxEvents.size} outbox events table for aggregate: $aggregate")

        outboxEvents.forEach { outboxEvent ->
            kafkaTemplate.send(topic, outboxEvent.message)
        }
    }

}
