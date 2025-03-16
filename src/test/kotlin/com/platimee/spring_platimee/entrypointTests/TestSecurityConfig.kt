// TestSecurityConfig.kt
package com.platimee.spring_platimee.entrypointTests

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@TestConfiguration
class TestSecurityConfig {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService {
        val manager = InMemoryUserDetailsManager()
        val testUser = User.withUsername("testuser")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build()
        manager.createUser(testUser)
        return manager
    }
}
