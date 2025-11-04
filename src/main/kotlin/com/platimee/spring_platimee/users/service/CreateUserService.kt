package com.platimee.spring_platimee.users.service

import com.platimee.spring_platimee.auth.account.email.MailService
import com.platimee.spring_platimee.auth.account.verification.VerificationService
import com.platimee.spring_platimee.users.exceptions.UserAlreadyExistsException
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserDtoMapper
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CreateUserService(
    private val userRepository: UserRepository,
    private val verificationService: VerificationService,
    private val mailService: MailService
) {

    private val logger = LoggerFactory.getLogger(CreateUserService::class.java)

    @Transactional
    fun addUser(userDTO: UserCreateDTO): UserResponseDTO {
        logger.info("Attempting to create user with username: ${userDTO.username}")

        userRepository.findByUsername(userDTO.username)?.let {
            throw UserAlreadyExistsException("User with username '${userDTO.username}' already exists.")
        }

        userRepository.findByEmail(userDTO.email)?.let {
            throw UserAlreadyExistsException("User with email '${userDTO.email}' already exists.")
        }

        val user = UserDtoMapper.toEntity(userDTO.copy(password = BCryptPasswordEncoder().encode(userDTO.password)))

        val savedUser = userRepository.save(user)
        logger.info("User created successfully: ${savedUser.userId}")

        // create verification token and send verification email
        val token = verificationService.createVerificationToken(savedUser.userId!!)
        mailService.sendVerificationEmail(savedUser.email, token.token)
        logger.info("Verification email sent to ${savedUser.email}")

        return UserDtoMapper.toResponseDto(savedUser)
    }
}