package com.platimee.spring_platimee.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
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
    @field:NotBlank(message = "Username must be between 2 and 50 characters.")
    @field:Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters.")
    val username: String,

    @field:NotBlank(message = "Email must be a valid format (e.g., user@example.com).")
    @field:Email(message = "Email must be a valid format (e.g., user@example.com).")
    @field:Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Email must be a valid format (e.g., user@example.com)."
    )
    val email: String,

    @field:NotBlank(message = "First name must be between 2 and 50 characters.")
    @field:Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    val firstName: String,

    @field:NotBlank(message = "Last name must be between 2 and 50 characters.")
    @field:Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    val lastName: String,

    @field:NotBlank(message = "Password must contain at least one letter, one number, one special character, and must not contain spaces.")
    @field:Pattern(
        //regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        regexp = "^(?!.*\\s)(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "Password must contain at least one letter, one number, one special character, and must not contain spaces."
    )
    val password: String,

    //val hashedPassword: String = BCryptPasswordEncoder().encode(password)
)

data class UserResponseDTO(
    @JsonProperty("id") val userId: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String
)