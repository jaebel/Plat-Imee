package com.platimee.spring_platimee.ratings.model

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class RatingUpdateDTO(
    @field:NotNull(message = "Rating cannot be null")
    @field:Min(value = 0, message = "Rating must be at least 0")
    @field:Max(value = 10, message = "Rating must be at most 10")
    val rating: Double
)
