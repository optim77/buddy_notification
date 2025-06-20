package com.sq.notify.notification.DTO

import java.util.UUID

data class LogoutRequest(
    val username: String,
    val userId: UUID,
    val token: String
)
