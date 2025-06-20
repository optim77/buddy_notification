package com.sq.notify.notification.DTO

import com.sq.notify.notification.entity.NotificationType
import java.time.LocalDateTime
import java.util.*

data class NotificationRequest(
    val consumerUsername: String,
    val consumerId: UUID,
    val broadcasterUsername: String,
    val broadcasterId: UUID,
    val type: NotificationType,
    val message: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    )
