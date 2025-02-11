package com.platimee.spring_platimee.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

data class UserUpdateDTO(
    @field:Size(min = 3, max = 50) val username: String? = null,
    @field:Email val email: String? = null,
    @field:Size(min = 2, max = 50) val firstName: String? = null,
    @field:Size(min = 2, max = 50) val lastName: String? = null,
    @field:Size(min = 8) val password: String? = null
)

data class UserCreateDTO(
    @field:NotBlank @field:Size(min = 3, max = 50) val username: String,
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 2, max = 50) val firstName: String,
    @field:NotBlank @field:Size(min = 2, max = 50) val lastName: String,
    @field:NotBlank @field:Size(min = 8) val password: String,

    val hashedPassword: String = BCryptPasswordEncoder().encode(password)
)

data class UserResponseDTO(
    @JsonProperty("id") val userId: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String
)