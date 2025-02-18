package com.platimee.spring_platimee.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserResponseDTO(
    @JsonProperty("id") val userId: Long,
    @JsonProperty("username") val username: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String
)