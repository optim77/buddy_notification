package com.sq.notify.security

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder


@Configuration
@EnableWebSocketMessageBroker
class WebSocketSecurityConfig(
    private val jwtWsService: JwtWsService
): WebSocketMessageBrokerConfigurer {

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
                val accessor = StompHeaderAccessor.wrap(message)
                val token = accessor.getFirstNativeHeader("Authorization")?.removePrefix("Bearer ")

                if (!token.isNullOrBlank()) {
                    val userDetails = jwtWsService.validateTokenAndGetUser(token)
                    val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    accessor.user = auth
                    SecurityContextHolder.getContext().authentication = auth
                }

                return message
            }
        })
    }
}