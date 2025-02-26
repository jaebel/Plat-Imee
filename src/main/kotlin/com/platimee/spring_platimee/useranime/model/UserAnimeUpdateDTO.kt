package com.platimee.spring_platimee.useranime.model

data class UserAnimeUpdateDTO(
    val status: UserAnimeStatus? = null,
    val rating: Double? = null,
    val episodesWatched: Int? = null
)
