package com.sq.notify.session.repository

import com.sq.notify.session.entity.Session
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SessionRepository: JpaRepository<Session, UUID> {
    fun findBySessionId(sessionId: UUID): Session?
}