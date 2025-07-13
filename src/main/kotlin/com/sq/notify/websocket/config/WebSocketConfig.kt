package com.sq.notify.websocket.config

import com.sq.notify.security.JwtChannelInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val jwtChannelInterceptor: JwtChannelInterceptor
): WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        println("WebSocket Config")
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        println("Registering Stomp Endpoints")
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:3000")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(jwtChannelInterceptor)
    }
}