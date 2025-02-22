package com.platimee.spring_platimee.anime.model

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class AnimeUpdateDTO(
    @field:Size(min = 2, max = 255, message = "Anime name must be between 2 and 255 characters.")
    val name: String? = null,

    // Allow updating the anime type if provided.
    val type: AnimeType? = null,

    @field:Min(value = 1, message = "Episodes must be at least 1.")
    val episodes: Int? = null,

    @field:DecimalMin(value = "0.0", message = "Rating must be between 0.0 and 10.0.")
    @field:DecimalMax(value = "10.0", message = "Rating must be between 0.0 and 10.0.")
    val rating: Double? = null,

    @field:Min(value = 0, message = "Members count must be non-negative.")
    val members: Int? = null,

    // Optional: if provided, update the genres using these genre IDs.
    val genres: List<Long>? = null
)
