package com.platimee.spring_platimee.recommendation.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RecResponseDTO(
    @JsonProperty("mal_id") val malId: Long,
)

data class AnimeEntryDTO(
    val malId: Long,
    // Default rating is set to 7 if not provided
    val rating: Double = 7.0
)

data class UserAnimeListDTO(
    val userId: Long,
    val animeList: List<AnimeEntryDTO>,
    val safeSearch: Boolean = false
)