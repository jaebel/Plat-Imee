package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.auth.account.exceptions.RateLimitException
import com.platimee.spring_platimee.users.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class VerificationService(
    private val tokenRepository: VerificationTokenRepository,
    private val userRepository: UserRepository,
) {

    private val cooldownMinutes = 1L  // user can only request a new email every 1 minute

    @Transactional
    fun createVerificationToken(userId: Long): VerificationToken {
        val user = userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("User not found")

        val recentToken = tokenRepository.findTopByUserOrderByCreatedAtDesc(user)
        if (recentToken != null && recentToken.createdAt.isAfter(LocalDateTime.now().minusMinutes(cooldownMinutes))) {
            throw RateLimitException("Too many requests — please wait before trying again.")
        }

        tokenRepository.deleteByUser(user)

        val token = VerificationToken(
            token = UUID.randomUUID().toString(),
            user = user,
            expiryDate = LocalDateTime.now().plusHours(24))

        tokenRepository.save(token)

        return token
    }

    @Transactional
    fun verifyAccount(tokenValue: String): String {
        val token = tokenRepository.findByToken(tokenValue)
            ?: throw IllegalArgumentException("Invalid verification token")

        if (token.used) {
            throw IllegalArgumentException("Token already used")
        }

        if (token.expiryDate.isBefore(LocalDateTime.now())) {
            throw IllegalArgumentException("Token expired")
        }

        val user = token.user
            ?: throw IllegalStateException("Token has no associated user")

        user.isVerified = true
        token.used = true // this probably isn't needed since it gets deleted after

        tokenRepository.deleteByUser(user) // delete token after use

        return "Account verified successfully."
    }
}