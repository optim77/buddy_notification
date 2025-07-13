package com.sq.notify.security

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtChannelInterceptor(
    private val jwtWsService: JwtWsService
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        if (StompCommand.CONNECT == accessor?.command) {
            val token = accessor.getFirstNativeHeader("Authorization")?.removePrefix("Bearer ")?.trim()
            if (!token.isNullOrBlank()) {
                val user = jwtWsService.validateTokenAndGetUser(token)
                accessor.user = UsernamePasswordAuthenticationToken(user, null, user.authorities)
            }
        }

        return message
    }
}