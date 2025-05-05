package com.platimee.spring_platimee.security

import com.platimee.spring_platimee.users.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // Load user from the database using my UserRepository
        // Ensure the User entity contains the username and the encoded password
        val userEntity = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        // Build and return a UserDetails object using Spring Security's User class
        return User(
            userEntity.username,
            userEntity.password,
            emptyList()
        )
    }
}
