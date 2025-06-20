package com.sq.notify.notification.controller

import com.sq.notify.notification.DTO.LogoutRequest
import com.sq.notify.notification.DTO.NotificationRequest
import com.sq.notify.notification.NotificationService
import com.sq.notify.notification.entity.Notification
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class NotificationController(
    private val notificationService: NotificationService
) {

    @PostMapping("/notification")
    fun receiveNotification(@RequestBody notification: NotificationRequest): ResponseEntity<Void> {
        notificationService.sendAndSave(notification);
        return ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/notification/logout")
    fun logout(@RequestBody notification: LogoutRequest): ResponseEntity<Void> {

    }
}