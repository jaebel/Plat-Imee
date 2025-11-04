package com.platimee.spring_platimee.auth.account.passwordreset

import com.platimee.spring_platimee.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
    fun findByToken(token: String): PasswordResetToken?
    fun deleteByUser(user: User)
    fun findTopByUserOrderByCreatedAtDesc(user: User): PasswordResetToken?

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now OR t.used = true")
    fun deleteAllExpiredOrUsed(now: LocalDateTime): Int
}
