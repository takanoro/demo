package xyz.btc.demo.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import xyz.btc.demo.model.TransferDto
import xyz.btc.demo.service.InboxService

@Component
class TransferListener(
    private val inboxService: InboxService,
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(TransferListener::class.java)

    @KafkaListener(topics = ["transfer_received"], groupId = "transfer-query")
    fun listen(message: String) {
        logger.info("Transfer received.")

        runBlocking {
            val transferDto = objectMapper.readValue<TransferDto>(message)

            inboxService.receiveEvent(transferDto.id, message, "TRANSFER")
        }
    }

}