package com.platimee.spring_platimee.auth.account.verification.service

import com.platimee.spring_platimee.auth.account.verification.repository.VerificationTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerificationTokenCleanupService(
    private val tokenRepository: VerificationTokenRepository
) {
    private val logger = LoggerFactory.getLogger(VerificationTokenCleanupService::class.java)

    // Runs once a day at 2 AM to remove expired or used tokens.
    @Scheduled(cron = "0 0 2 * * *")
    fun cleanupExpiredTokens() {
        val now = LocalDateTime.now()
        val deletedCount = tokenRepository.deleteAllExpiredOrUsed(now)
        if (deletedCount > 0) {
            logger.info("Deleted $deletedCount expired or used verification tokens.")
        }
    }
}
