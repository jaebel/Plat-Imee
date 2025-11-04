package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository // spring-boot-starter-data-jpa autoconfigures so we don't actually need this annotation
interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByToken(token: String): VerificationToken?
    fun deleteByUser(user: User)
    fun findTopByUserOrderByCreatedAtDesc(user: User): VerificationToken?

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken t WHERE t.expiryDate < :now OR t.used = true")
    fun deleteAllExpiredOrUsed(now: LocalDateTime): Int
}