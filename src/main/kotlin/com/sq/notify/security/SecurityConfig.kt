package com.sq.notify.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.crypto.spec.SecretKeySpec
import org.springframework.web.filter.CorsFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${notification-service.auth-token}")
    private val authToken: String,
    private val backendAuth: InternalServiceAuthFilter,
    private val wsAuth: JwtAuthenticationFilter,
) {

    @Bean
    @Order(1)
    fun backendSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/notification/**")
            .csrf { it.disable() }
            .authorizeHttpRequests {auth ->
                auth
                    .requestMatchers(
                        "/notification/register",
                        "/notification/logout",
                        "/notification/logout/all",
                        "/notification/notify"
                    ).authenticated()
            }
            .addFilterBefore(backendAuth, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    @Order(2)
    fun websocketSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/ws/**")
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .csrf { csrf -> csrf.disable() }
            .addFilterBefore(wsAuth, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withSecretKey(
            SecretKeySpec(authToken.toByteArray(), "HS512")
        ).build()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:3000")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("Authorization", "Content-Type")
        config.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

}