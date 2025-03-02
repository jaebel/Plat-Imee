package com.platimee.spring_platimee.anime.model

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AnimeCreateDTO(
    // MAL ID might be provided or set later by a seeder.
    val malId: Long? = null,

    @field:NotBlank(message = "Anime name cannot be blank.")
    @field:Size(min = 2, max = 255, message = "Anime name must be between 2 and 255 characters.")
    val name: String,

    // I may or maynot need this
    val englishName: String? = null,
    val japaneseName: String? = null,

    @field:NotNull(message = "Anime type must be specified.")
    val type: AnimeType,

    @field:Min(value = 1, message = "Episodes must be at least 1.")
    val episodes: Int? = null,

    @field:DecimalMin(value = "0.0", message = "Score must be between 0.0 and 10.0.")
    @field:DecimalMax(value = "10.0", message = "Score must be between 0.0 and 10.0.")
    val score: Double? = null,

    @field:Min(value = 0, message = "Members count must be non-negative.")
    val members: Int? = null,

    // Optional airing information
    val aired: String? = null,
    val premiered: String? = null,

    // Adding genres as a list of genre IDs (not comma-separated string here)
    @field:NotNull(message = "Genres must be specified.")
    val genres: List<Long>
)