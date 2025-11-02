package com.platimee.spring_platimee.auth.login.model

data class LoginResponseDTO(
    val token: String,
    val user: LoggedInUserDTO
)

data class LoggedInUserDTO(
    val userId: Long,
    val username: String,
    val email: String
)
