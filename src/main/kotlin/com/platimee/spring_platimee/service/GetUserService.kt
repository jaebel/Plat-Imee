package com.platimee.spring_platimee.service

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.model.UserDtoMapper
import com.platimee.spring_platimee.model.UserResponseDTO
import com.platimee.spring_platimee.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class GetUserService(private val userRepository: UserRepository) {

    fun getAll(): List<UserResponseDTO> {
        val users = userRepository.findAll()
        if (users.isEmpty()) {
            throw EntityNotFoundException("No users found")
        }
        return users.map { UserDtoMapper.toResponseDto(it) }
    }

    fun getById(userId: Long): UserResponseDTO {
        val user = userRepository.findByUserId(userId)
            ?: throw EntityNotFoundException("User with ID $userId not found")
        return UserDtoMapper.toResponseDto(user)
    }
}