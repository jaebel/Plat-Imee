package com.platimee.spring_platimee.auth.service

import JwtUtil
import com.platimee.spring_platimee.auth.model.LoggedInUserDTO
import com.platimee.spring_platimee.auth.model.LoginRequestDTO
import com.platimee.spring_platimee.auth.model.LoginResponseDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun login(loginRequest: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByUsername(loginRequest.username)
            ?: throw EntityNotFoundException("User with username ${loginRequest.username} not found")

        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        // Generate JWT token for the authenticated user
        val token = JwtUtil.generateToken(user.username)

        val loggedInUser = LoggedInUserDTO(
            userId = user.userId!!,
            username = user.username,
            email = user.email
        )

        return LoginResponseDTO(token = token, user = loggedInUser)
    }
}
