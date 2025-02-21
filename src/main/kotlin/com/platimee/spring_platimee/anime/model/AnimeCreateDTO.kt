package com.platimee.spring_platimee.anime.model

import jakarta.validation.constraints.*

data class AnimeCreateDTO(
    @field:NotBlank(message = "Anime name cannot be blank.")
    @field:Size(min = 2, max = 255, message = "Anime name must be between 2 and 255 characters.")
    val name: String,

    @field:NotNull(message = "Anime type must be specified.")
    val type: AnimeType,

    @field:Min(value = 1, message = "Episodes must be at least 1.")
    val episodes: Int? = null,

    @field:DecimalMin(value = "0.0", message = "Rating must be between 0.0 and 10.0.")
    @field:DecimalMax(value = "10.0", message = "Rating must be between 0.0 and 10.0.")
    val rating: Double? = null,

    @field:Min(value = 0, message = "Members count must be non-negative.")
    val members: Int? = null,

    // Adding genres (either as names or ids)
    @field:NotNull(message = "Genres must be specified.")
    val genres: List<Long>,
)