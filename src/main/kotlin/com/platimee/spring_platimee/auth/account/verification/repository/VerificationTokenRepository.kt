package com.platimee.spring_platimee.auth.account.verification.repository

import com.platimee.spring_platimee.auth.account.verification.model.VerificationToken
import com.platimee.spring_platimee.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.Optional

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByToken(token: String): VerificationToken?
    fun deleteByUser(user: User)
    fun findTopByUserOrderByCreatedAtDesc(user: User): VerificationToken?

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken t WHERE t.expiryDate < :now OR t.used = true")
    fun deleteAllExpiredOrUsed(now: LocalDateTime): Int
}