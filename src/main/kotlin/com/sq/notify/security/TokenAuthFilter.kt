package com.sq.notify.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class TokenAuthFilter(private val expectedToken: String) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val expected = "Bearer $expectedToken"

        if (authHeader == null || authHeader != expected) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return
        }

        val auth = UsernamePasswordAuthenticationToken("system", null, emptyList())
        auth.isAuthenticated = true
        SecurityContextHolder.getContext().authentication = auth

        filterChain.doFilter(request, response)
    }
}