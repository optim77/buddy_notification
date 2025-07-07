package com.sq.notify.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtWsService: JwtWsService
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        return  path.startsWith("/gen-token") || request.method == "GET"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()

        try {
            val userDetails = jwtWsService.validateTokenAndGetUser(token)

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }

            SecurityContextHolder.getContext().authentication = authentication

        } catch (ex: Exception) {
            // Możesz logować błąd albo dodać 401 odpowiedź, jeśli chcesz
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}
