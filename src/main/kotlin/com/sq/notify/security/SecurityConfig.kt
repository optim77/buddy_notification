package com.sq.notify.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${notification-service.auth-token}")
    private val authToken: String
) {

    @Bean
    fun securityWebFilterChain(http: HttpSecurity): DefaultSecurityFilterChain? {
        return http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .addFilterBefore(TokenAuthFilter(authToken), BasicAuthenticationFilter::class.java)
            .build()
    }

}