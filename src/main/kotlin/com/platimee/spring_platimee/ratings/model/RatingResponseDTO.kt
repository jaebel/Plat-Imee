package com.platimee.spring_platimee.ratings.model

import java.time.Instant

data class RatingResponseDTO(
    val id: Long,
    val userId: Long,
    val animeId: Long,
    val rating: Double,
    val ratedAt: Instant
)
