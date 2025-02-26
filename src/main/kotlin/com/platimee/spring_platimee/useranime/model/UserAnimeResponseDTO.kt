package com.platimee.spring_platimee.useranime.model

data class UserAnimeResponseDTO(
    val id: Long,
    val userId: Long,
    val animeId: Long,
    val status: UserAnimeStatus?,
    val rating: Double?,
    val episodesWatched: Int?
)