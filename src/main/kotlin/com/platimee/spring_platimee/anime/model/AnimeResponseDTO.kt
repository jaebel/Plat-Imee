package com.platimee.spring_platimee.anime.model

import java.time.Instant

data class AnimeResponseDTO(
    val animeId: Long,
    val name: String,
    val type: AnimeType,
    val episodes: Int?,
    val rating: Double?,
    val members: Int?,
    val genres: List<String>, // List of genre names
    val createdDate: Instant,
    val updatedDate: Instant
)
