package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.users.model.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "verification_tokens")
class VerificationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var token: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(nullable = false)
    var expiryDate: LocalDateTime = LocalDateTime.now().plusHours(24),

    @Column(nullable = false)
    var used: Boolean = false,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)