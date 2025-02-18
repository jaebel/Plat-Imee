package com.platimee.spring_platimee.service

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.model.UserDtoMapper
import com.platimee.spring_platimee.model.UserResponseDTO
import com.platimee.spring_platimee.model.UserUpdateDTO
import com.platimee.spring_platimee.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UpdateUserService(private val userRepository: UserRepository) {

    // @Transactional
    fun updateUser(userId: Long, userUpdateRequest: UserUpdateDTO): UserResponseDTO {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with ID $userId not found") }

        userUpdateRequest.username?.let { user.username = it }
        userUpdateRequest.email?.let { user.email = it }
        userUpdateRequest.firstName?.let { user.firstName = it }
        userUpdateRequest.lastName?.let { user.lastName = it }
        userUpdateRequest.password?.let { user.password = BCryptPasswordEncoder().encode(it) }

        val savedUser = userRepository.save(user)
        return UserDtoMapper.toResponseDto(savedUser)
    }
}