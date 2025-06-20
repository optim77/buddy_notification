package com.sq.notify.notification

import com.sq.notify.notification.DTO.NotificationRequest
import com.sq.notify.notification.entity.Notification
import com.sq.notify.notification.repository.NotificationRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val messagingTemplate: SimpMessagingTemplate) {

    fun sendAndSave(event: NotificationRequest) {
        val notification = Notification(
            type=event.type,
            message=event.message,
            consumerId=event.consumerId,
            consumerUsername=event.consumerUsername,
            broadcasterId=event.broadcasterId,
            broadcasterUsername=event.broadcasterUsername
        )
        notificationRepository.save(notification)

        messagingTemplate.convertAndSendToUser(event.consumerUsername, "/topic/notifications/${notification.consumerId}", event)

    }

}