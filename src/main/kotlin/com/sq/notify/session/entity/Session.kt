package com.sq.notify.session.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    val sessionId: UUID,
    val userId: UUID,
    val sub: String,
    val iat: Long,
    val exp: Long,
)
