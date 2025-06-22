package com.sq.notify.security

import com.sq.notify.session.repository.SessionRepository
import com.sq.notify.user.CustomUserDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtWsService(
    @Value("\${jwt.secret}") private val secret: String,
    private val sessionRepository: SessionRepository
) {
    fun validateTokenAndGetUser(token: String): UserDetails {
        val key = Keys.hmacShaKeyFor(secret.toByteArray())

        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject
        val userId = claims["user_id"]?.toString() ?: throw IllegalArgumentException("Missing user_id")
        val role = claims["role"]?.toString() ?: "USER"
        val sessionId = claims["session_id"]?.toString() ?: ""
        val sub = claims["sub"]?.toString()
        val exp = claims["exp"]?.toString()?.toLong()

        val session = sessionRepository.findBySessionId(UUID.fromString(sessionId))
        if (session != null && sub == session.sub && exp != null && session.exp > exp) {
            return CustomUserDetails(UUID.fromString(userId), username, role)
        }
        throw IllegalArgumentException("User not found")
    }
}