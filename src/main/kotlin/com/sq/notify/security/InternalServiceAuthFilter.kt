package com.sq.notify.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class InternalServiceAuthFilter(
    @Value("\${notification-service.auth-token}") private val internalToken: String
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return !listOf(
            "/notification/register",
            "/notification/logout",
            "/notification/logout/all",
            "/notification/notify"
        ).any { path.startsWith(it) }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing token")
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()

        if (token != internalToken) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid internal token")
            return
        }

        val authentication = UsernamePasswordAuthenticationToken(
            "internal-service",
            null,
            listOf()
        )
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}