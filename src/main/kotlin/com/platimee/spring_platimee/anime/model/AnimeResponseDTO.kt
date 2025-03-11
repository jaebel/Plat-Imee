package com.platimee.spring_platimee.anime.model

import java.time.Instant

data class AnimeResponseDTO(
    val malId: Long,           // MAL ID from the CSV
    val name: String,
    val englishName: String?,   // English name of the anime
    val japaneseName: String?,  // Japanese name of the anime
    val type: AnimeType?,       // TV, Movie, OVA, etc.
    val episodes: Int?,         // Number of episodes
    val score: Double?,         // Renamed from "rating" to "score"
    val aired: String?,         // Broadcast date as a string
    val premiered: String?,     // Season premiere (e.g., "Fall 2004")
    val genres: List<String>,   // List of genre names
    val createdDate: Instant,
    val updatedDate: Instant
)
