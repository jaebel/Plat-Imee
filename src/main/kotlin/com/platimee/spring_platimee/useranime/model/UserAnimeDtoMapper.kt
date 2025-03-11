package com.platimee.spring_platimee.useranime.model

import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.users.model.User

object UserAnimeDtoMapper {

    // Map entity to response DTO
    fun toResponseDTO(userAnime: UserAnime): UserAnimeResponseDTO {
        return UserAnimeResponseDTO(
            id = userAnime.id ?: 0L,
            userId = userAnime.user.userId ?: 0L,
            malId = userAnime.anime.malId,
            status = userAnime.status,
            rating = userAnime.rating,
            episodesWatched = userAnime.episodesWatched
        )
    }

    // Map create DTO to entity (without setting user and anime references; these must be fetched in the service)
    fun toEntity(dto: UserAnimeCreateDTO, user: User, anime: Anime): UserAnime {
        return UserAnime(
            user = user,
            anime = anime,
            status = dto.status ?: UserAnimeStatus.WATCHING  // Default to WATCHING
        )
    }

    // Update an existing entity from an update DTO
    fun updateEntityFromDTO(userAnime: UserAnime, dto: UserAnimeUpdateDTO): UserAnime {
        dto.status?.let { userAnime.status = it }
        dto.rating?.let { userAnime.rating = it }
        dto.episodesWatched?.let { userAnime.episodesWatched = it }
        return userAnime
    }
}