package xyz.btc.demo.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.r2dbc.spi.ConnectionFactory
import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.r2dbc.R2dbcLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import xyz.btc.demo.configuration.properties.InboxProperties

@Configuration
@EnableConfigurationProperties(InboxProperties::class)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m", defaultLockAtLeastFor = "20s")
class ApplicationConfiguration {

    @Bean
    fun objectMapper() = ObjectMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    }

    @Bean
    fun lockProvider(connectionFactory: ConnectionFactory): LockProvider =
        R2dbcLockProvider(connectionFactory)

}
