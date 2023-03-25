package xyz.btc.demo.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "inbox")
data class InboxProperties(
    val aggregate: Map<String, AggregateInboxSetting>
)

data class AggregateInboxSetting(
    val batchSize: Int
)