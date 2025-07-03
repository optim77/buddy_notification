package com.sq.notify.notification

import com.sq.notify.notification.DTO.LogoutRequest
import com.sq.notify.notification.DTO.NotificationRequest
import com.sq.notify.notification.DTO.RegisterRequest
import com.sq.notify.notification.entity.Notification
import com.sq.notify.notification.repository.NotificationRepository
import com.sq.notify.session.entity.Session
import com.sq.notify.session.repository.SessionRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val messagingTemplate: SimpMessagingTemplate,
    private val sessionRepository: SessionRepository) {

    @Transactional
    fun register(request: RegisterRequest) {
        val session = sessionRepository.findById(request.sessionId).orElse(
            Session(
                sessionId = request.sessionId,
                sub = request.sub,
                iat = request.iat,
                exp = request.exp,
                userId = request.userId,
            )
        ).apply {
            sub = request.sub
            iat = request.iat
            exp = request.exp
            userId = request.userId
        }

        sessionRepository.save(session)
    }


    fun logout(request: LogoutRequest) {
        val session = sessionRepository.findBySessionId(request.sessionId)
        if (session != null) {
            sessionRepository.delete(session)
        }

    }

    @Transactional
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