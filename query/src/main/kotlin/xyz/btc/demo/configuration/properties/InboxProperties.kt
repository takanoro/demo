package xyz.btc.demo.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "inbox")
@ConstructorBinding
data class InboxProperties(
    val aggregate: Map<String, AggregateInboxSetting>
)

data class AggregateInboxSetting(
    val batchSize: Int
)