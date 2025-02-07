package com.platimee.spring_platimee.service

import com.platimee.spring_platimee.expections.UserAlreadyExistsException
import org.springframework.stereotype.Service
import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.repository.UserRepository
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory

@Service
class CreateUserService(private val userRepository: UserRepository) {

    private val logger = LoggerFactory.getLogger(CreateUserService::class.java)

    @Transactional
    fun addUser(user: User): User {
        logger.info("Attempting to create user: ${user.username}")

        userRepository.findByUsername(user.username)?.let { existingUser ->
            if (isDuplicateUser(existingUser, user)) {
                logger.info("User already exists with the same details: ${user.username}")
                return existingUser
            } else {
                logger.warn("Username already exists with different details: ${user.username}")
                throw UserAlreadyExistsException("User with username '${user.username}' already exists with different details.")
            }
        }

        val savedUser = userRepository.save(user)
        logger.info("User created successfully: ${savedUser.userId}")

        return savedUser
    }

    private fun isDuplicateUser(existingUser: User, newUser: User): Boolean {
        return existingUser.username == newUser.username &&
                existingUser.email == newUser.email
    }
}