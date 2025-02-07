package com.platimee.spring_platimee.service

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class GetUserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> {
        val users = userRepository.findAll()
        if (users.isEmpty()) {
            throw EntityNotFoundException("No users found")
        }
        return users
    }

    fun getById(userId: Long): User {
        return userRepository.findByUserId(userId)
            ?: throw EntityNotFoundException("User with ID $userId not found")
    }
}