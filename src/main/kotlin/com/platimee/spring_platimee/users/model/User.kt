package com.platimee.spring_platimee.users.model

import com.platimee.spring_platimee.auth.account.passwordreset.PasswordResetToken
import com.platimee.spring_platimee.auth.account.verification.VerificationToken
import com.platimee.spring_platimee.useranime.model.UserAnime
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "myGenerator")
    @SequenceGenerator(name = "myGenerator", sequenceName = "mySequenceForGenerator", allocationSize = 1)
    @Column(name = "user_id")
    var userId: Long? = null,

    @Column(name = "username", unique = true, nullable = false)
    var username: String,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "created_date")
    @CreatedDate
    var createdDate: Instant = Instant.now(),

    @Column(name = "updated_date")
    @LastModifiedDate
    var updatedDate: Instant = Instant.now(),

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var verificationTokens: MutableList<VerificationToken> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var passwordResetTokens: MutableList<PasswordResetToken> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userAnimeList: MutableList<UserAnime> = mutableListOf()
)