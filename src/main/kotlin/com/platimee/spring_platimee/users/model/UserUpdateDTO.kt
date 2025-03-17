package com.platimee.spring_platimee.users.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = false)
data class UserUpdateDTO(

    @field:Email(message = "Email must be a valid format (e.g., user@example.com).")
    @field:Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "Email must be a valid format (e.g., user@example.com)."
    )
    val email: String? = null,

    @field:Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    val firstName: String? = null,

    @field:Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    val lastName: String? = null,

    @field:Size(min = 8, message = "Password must be at least 8 characters long.")
    @field:Pattern(
        regexp = "^(?!.*\\s)(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "Password must contain at least one letter, one number, one special character, and must not contain spaces."
    )
    val password: String? = null
)

