package com.platimee.spring_platimee.auth.account.passwordreset

import com.platimee.spring_platimee.auth.account.exceptions.RateLimitException
import com.platimee.spring_platimee.users.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class PasswordResetService(
    private val tokenRepository: PasswordResetTokenRepository,
    private val userRepository: UserRepository,
) {

    private val cooldownMinutes = 5L

    @Transactional
    fun createPasswordResetToken(email: String): PasswordResetToken {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: ${email}")

        val recentToken = tokenRepository.findTopByUserOrderByCreatedAtDesc(user)
        if (recentToken != null && recentToken.createdAt.isAfter(LocalDateTime.now().minusMinutes(cooldownMinutes))) {
            throw RateLimitException("Too many requests — please wait before trying again.")
        }

        // Clear old tokens via relationship
        user.passwordResetTokens.clear()

        val token = PasswordResetToken(
            token = UUID.randomUUID().toString(),
            user = user,
            expiryDate = LocalDateTime.now().plusHours(1)
        )

        user.passwordResetTokens.add(token)

        userRepository.save(user) // cascade persists token
        return token
    }

    @Transactional
    fun resetPassword(tokenValue: String, newPassword: String): String {
        val token = tokenRepository.findByToken(tokenValue)
            ?: throw IllegalArgumentException("Invalid password reset token")

        if (token.used) throw IllegalArgumentException("Token already used")
        if (token.expiryDate.isBefore(LocalDateTime.now())) throw IllegalArgumentException("Token expired")

        val user = token.user ?: throw IllegalStateException("Token has no associated user")

        user.password = BCryptPasswordEncoder().encode(newPassword)
        user.passwordResetTokens.clear() // delete all reset tokens

        userRepository.save(user)

        return "Password has been reset successfully."
    }

}
