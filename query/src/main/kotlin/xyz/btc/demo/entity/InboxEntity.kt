package xyz.btc.demo.entity

import java.time.Instant

data class InboxEntity(
    val id: String,
    val aggregate: String,
    val message: String,
    val createdAt: Instant
)