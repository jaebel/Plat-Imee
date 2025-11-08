package com.platimee.spring_platimee.users.service

import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteUserService(private val userRepository: UserRepository) {

    @Transactional
    fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with ID $userId not found") }

        userRepository.delete(user)
    }

    @Transactional
    fun deleteCurrentUserByToken(username: String) {
        val user = userRepository.findByUsername(username)
            ?: return
        userRepository.delete(user)
    }
}