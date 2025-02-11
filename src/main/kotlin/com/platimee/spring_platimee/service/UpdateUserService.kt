package com.platimee.spring_platimee.service

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.model.UserUpdateDTO
import com.platimee.spring_platimee.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class UpdateUserService(private val userRepository: UserRepository) {

    // @Transactional
    fun updateUser(userId: Long, userUpdateRequest: UserUpdateDTO): User {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with ID $userId not found") }

        userUpdateRequest.username?.let { user.username = it }
        userUpdateRequest.email?.let { user.email = it }
        userUpdateRequest.firstName?.let { user.firstName = it }
        userUpdateRequest.lastName?.let { user.lastName = it }
        userUpdateRequest.password?.let { user.password = it }

        return userRepository.save(user)
    }
}