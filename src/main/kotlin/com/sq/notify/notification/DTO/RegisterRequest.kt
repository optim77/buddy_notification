package com.sq.notify.notification.DTO

import java.util.*

data class RegisterRequest(
    val userId: UUID,
    val sessionId: UUID,
    val sub: String,
    val iat: Long,
    val exp: Long,
)