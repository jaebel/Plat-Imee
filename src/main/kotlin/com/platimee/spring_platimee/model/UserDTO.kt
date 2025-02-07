package com.platimee.spring_platimee.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserDTO(
    @field:Size(min = 3, max = 50) val username: String? = null,
    @field:Email val email: String? = null,
    @field:Size(min = 2, max = 50) val firstName: String? = null,
    @field:Size(min = 2, max = 50) val lastName: String? = null,
    @field:Size(min = 8) val password: String? = null
)