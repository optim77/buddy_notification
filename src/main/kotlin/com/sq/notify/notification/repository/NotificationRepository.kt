package com.sq.notify.notification.repository

import com.sq.notify.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationRepository: JpaRepository<Notification, UUID> {
    fun findAllByConsumer(consumer: UUID): MutableList<Notification>
    fun findAllUnreadByConsumer(consumer: UUID): List<Notification>
}