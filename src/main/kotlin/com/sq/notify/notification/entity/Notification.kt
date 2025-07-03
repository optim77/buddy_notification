package com.sq.notify.notification.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @Enumerated(EnumType.STRING)
    val type: NotificationType,
    val message: String,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),
    val consumerUsername: String,
    val consumerId: UUID,
    val broadcasterUsername: String,
    val broadcasterId: UUID,
    val read: Boolean = false,
)