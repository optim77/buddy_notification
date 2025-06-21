package com.sq.notify.notification.DTO

import java.util.UUID

data class LogoutRequest(
    val sub: String,
    val userId: UUID,
    val sessionId: UUID
)
