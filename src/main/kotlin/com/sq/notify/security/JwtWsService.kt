package com.sq.notify.security

import com.sq.notify.user.CustomUserDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtWsService(
    @Value("\${jwt.secret}") private val secret: String
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

        return CustomUserDetails(UUID.fromString(userId), username, role)

    }
}