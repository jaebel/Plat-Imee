package com.platimee.spring_platimee.auth.account

import com.platimee.spring_platimee.auth.account.passwordreset.PasswordResetTokenRepository
import com.platimee.spring_platimee.auth.account.verification.VerificationTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TokenCleanupService(
    private val verificationTokenRepository: VerificationTokenRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository
) {
    private val logger = LoggerFactory.getLogger(TokenCleanupService::class.java)

    // Runs once a day at 2 AM to remove expired or used tokens.
    @Scheduled(cron = "0 0 2 * * *")
    fun cleanupExpiredAccountValidationTokens() {
        val now = LocalDateTime.now()
        val deletedCount = verificationTokenRepository.deleteAllExpiredOrUsed(now)
        if (deletedCount > 0) {
            logger.info("Deleted $deletedCount expired or used verification tokens.")
        }
    }

    // Runs every hour to remove expired or used tokens.
    @Scheduled(cron = "0 0 * * * *")
    fun cleanupExpiredPasswordRecoveryTokens() {
        val now = LocalDateTime.now()
        val deletedCount = passwordResetTokenRepository.deleteAllExpiredOrUsed(now)
        if (deletedCount > 0) {
            logger.info("Deleted $deletedCount expired or used verification tokens.")
        }
    }

}