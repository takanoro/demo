package xyz.btc.demo.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "outbox")
@ConstructorBinding
data class OutboxProperties(
    val aggregate: Map<String, AggregateOutboxSetting>
)

data class AggregateOutboxSetting(
    val topic: String,
    val batchSize: Int
)