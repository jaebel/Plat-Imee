package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.auth.account.exceptions.RateLimitException
import com.platimee.spring_platimee.users.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class VerificationService(
    private val tokenRepository: VerificationTokenRepository,
    private val userRepository: UserRepository
) {

    private val cooldownMinutes = 1L // user can only request a new email every 1 minute
    private val logger = LoggerFactory.getLogger(VerificationService::class.java)

    @Transactional
    fun createVerificationToken(userId: Long): VerificationToken {
        logger.info("Creating verification token for userId={}", userId)
        val user = userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("User not found")

        // Enforce rate limit
        val recentToken = tokenRepository.findTopByUserOrderByCreatedAtDesc(user)
        if (recentToken != null && recentToken.createdAt.isAfter(LocalDateTime.now().minusMinutes(cooldownMinutes))) {
            throw RateLimitException("Too many requests — please wait before trying again.")
        }

        // Remove old tokens using the relationship instead of repository delete
        user.verificationTokens.clear()

        // Create and associate new token
        val newToken = VerificationToken(
            token = UUID.randomUUID().toString(),
            user = user,
            expiryDate = LocalDateTime.now().plusHours(24)
        )

        user.verificationTokens.add(newToken)

        // Persist through user, cascade will handle token
        userRepository.save(user)

        return newToken
    }


    @Transactional
    fun verifyAccount(tokenValue: String): String {
        logger.info("Verifying account with token={}", tokenValue)

        val token = tokenRepository.findByToken(tokenValue)
            ?: run{
                logger.warn("[verifyAccount] No verification token found for token='{}'", tokenValue)
                throw IllegalArgumentException("Invalid verification token")
            }

        if (token.used) {
            logger.warn("[verifyAccount] Token='{}' already marked as used for userId={}", token.token, token.user?.userId)
            throw IllegalArgumentException("Token already used")
        }

        if (token.expiryDate.isBefore(LocalDateTime.now())) {
            logger.warn(
                "[verifyAccount] Token='{}' expired at {} (current time={}) for userId={}",
                token.token, token.expiryDate, LocalDateTime.now(), token.user?.userId
            )
            throw IllegalArgumentException("Token expired")
        }

        val user = token.user ?: run {
            logger.error("[verifyAccount] Token='{}' has no associated user record", token.token)
            throw IllegalStateException("Token has no associated user")
        }

        logger.info("[verifyAccount] Verifying user='{}' (id={}) using token='{}'", user.email, user.userId, token.token)

        user.isVerified = true
        token.used = true // this probably isn't needed since it gets deleted after

        // Clean up tokens after successful verification
        tokenRepository.deleteByUser(user)

        logger.info("🎉 [verifyAccount] Verification completed successfully for user='{}' (token='{}')", user.email, token.token)

        return "Account verified successfully."
    }
}
