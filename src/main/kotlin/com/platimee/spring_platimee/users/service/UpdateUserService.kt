package com.platimee.spring_platimee.users.service

import com.platimee.spring_platimee.users.model.UserDtoMapper
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.security.access.AccessDeniedException

@Service
class UpdateUserService(private val userRepository: UserRepository) {

    // @Transactional
    fun updateUser(userId: Long, userUpdateRequest: UserUpdateDTO, authenticatedUsername: String): UserResponseDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with ID $userId not found") }

        if (user.username != authenticatedUsername) {
            throw AccessDeniedException("You are not authorized to update this profile")
        }

        userUpdateRequest.username?.let { user.username = it }
        userUpdateRequest.email?.let { user.email = it }
        userUpdateRequest.firstName?.let { user.firstName = it }
        userUpdateRequest.lastName?.let { user.lastName = it }
        userUpdateRequest.password?.let { user.password = BCryptPasswordEncoder().encode(it) }

        val savedUser = userRepository.save(user)
        return UserDtoMapper.toResponseDto(savedUser)
    }
}