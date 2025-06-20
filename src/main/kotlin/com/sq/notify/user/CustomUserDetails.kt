package com.sq.notify.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


data class CustomUserDetails(
    val userId: UUID,
    private val username: String,
    private val role: String
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf { role }

    override fun getPassword(): String? = null

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}