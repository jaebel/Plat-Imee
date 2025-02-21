package com.platimee.spring_platimee.users.repository

import com.platimee.spring_platimee.users.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByUserId(userId: Long): User?
    fun findByEmail(email: String): User?
}