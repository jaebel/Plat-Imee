package com.platimee.spring_platimee.anime.model

import java.time.Instant

data class AnimeResponseDTO(
    val malId: Long,           // MAL ID from the CSV
    val name: String,
    val englishName: String?,
    val japaneseName: String?,
    val type: AnimeType?,       // TV, Movie, OVA, etc.
    val episodes: Int?,
    val score: Double?,
    val aired: String?,
    val premiered: String?,     // Season premiere (e.g., "Fall 2004")
    val genres: List<String>,   // List of genre names
    val createdDate: Instant,
    val updatedDate: Instant
)
