package com.platimee.spring_platimee.auth.account.verification.repository

import com.platimee.spring_platimee.auth.account.verification.model.VerificationToken
import com.platimee.spring_platimee.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByToken(token: String): VerificationToken?
    fun deleteByUser(user: User)
    fun findTopByUserOrderByCreatedAtDesc(user: User): VerificationToken?
}