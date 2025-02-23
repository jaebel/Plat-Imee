package com.platimee.spring_platimee.ratings.model

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull

data class RatingCreateDTO(
    @field:NotNull(message = "User ID is required.")
    val userId: Long,

    @field:NotNull(message = "Anime ID is required.")
    val animeId: Long,

    @field:NotNull(message = "Rating is required.")
    @field:DecimalMin(value = "0.0", message = "Rating must be between 0.0 and 10.0.")
    @field:DecimalMax(value = "10.0", message = "Rating must be between 0.0 and 10.0.")
    val rating: Double
)
