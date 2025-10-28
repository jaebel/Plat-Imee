package com.platimee.spring_platimee.users.service

import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class DeleteUserService(private val userRepository: UserRepository) {

    fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with ID $userId not found") }

        userRepository.delete(user)
    }

    fun deleteCurrentUserByToken(username: String) {
        val user = userRepository.findByUsername(username)
            ?: throw EntityNotFoundException("User not found")

        userRepository.delete(user)
    }
}