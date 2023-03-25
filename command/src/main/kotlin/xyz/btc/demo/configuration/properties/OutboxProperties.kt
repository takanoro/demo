package xyz.btc.demo.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "outbox")
data class OutboxProperties(
    val aggregate: Map<String, AggregateOutboxSetting>
)

data class AggregateOutboxSetting(
    val topic: String,
    val batchSize: Int
)