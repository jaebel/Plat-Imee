package com.platimee.spring_platimee.useranime.model

import jakarta.validation.constraints.NotNull

data class UserAnimeCreateDTO(
    @field:NotNull(message = "User ID is required")
    val userId: Long,

    @field:NotNull(message = "Anime mal ID is required")
    val malId: Long,

    // Defaulted in the mapper
    val status: UserAnimeStatus? = null
)

