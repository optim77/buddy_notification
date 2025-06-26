package com.sq.notify.notification.controller

import com.sq.notify.notification.DTO.LogoutRequest
import com.sq.notify.notification.DTO.NotificationRequest
import com.sq.notify.notification.DTO.RegisterRequest
import com.sq.notify.notification.NotificationService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping
class NotificationController(
    private val notificationService: NotificationService,
    @Value("\${notification-service.auth-token}") private val authToken: String
) {

    @PostMapping("/notification/register")
    fun registerSession(@RequestBody request: RegisterRequest): ResponseEntity<Void> {
        notificationService.register(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/notification/logout")
    fun logout(@RequestBody notification: LogoutRequest): ResponseEntity<Void> {
        notificationService.logout(notification)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/notification/logout/all")
    fun logoutAllSession(@RequestBody request: List<LogoutRequest>): ResponseEntity<Void> {
        request.forEach {
            notificationService.logout(it)
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/notification")
    fun receiveNotification(@RequestBody notification: NotificationRequest): ResponseEntity<Void> {
        notificationService.sendAndSave(notification)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/health")
    fun health(): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @GetMapping("/gen-token")
    fun gen(): String {
        val now = Date()
        val exp = Date(now.time + 3600000000000)

        return Jwts.builder()
            .setSubject("notifier")
            .setIssuer("buddy_notify")
            .setAudience("backend")
            .claim("Role", "SYSTEM")
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, authToken.toByteArray())
            .compact()
    }


}