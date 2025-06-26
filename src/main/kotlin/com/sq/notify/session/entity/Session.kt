package com.sq.notify.session.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class Session(
    @Id
    val sessionId: UUID,
    var userId: UUID,
    var sub: String,
    var iat: Long,
    var exp: Long,
)
