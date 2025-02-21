package com.platimee.spring_platimee.anime.model

object AnimeDtoMapper {
    // Now simply maps fields without handling genres.
    fun toEntity(dto: AnimeCreateDTO): Anime {
        return Anime(
            name = dto.name,
            type = dto.type,
            episodes = dto.episodes,
            rating = dto.rating,
            members = dto.members,
            genres = mutableSetOf()  // Will be set in the service layer
        )
    }

    fun toResponseDto(entity: Anime): AnimeResponseDTO {
        val genreNames = entity.genres.map { it.name }
        return AnimeResponseDTO(
            animeId = entity.animeId!!,
            name = entity.name,
            type = entity.type,
            episodes = entity.episodes,
            rating = entity.rating,
            members = entity.members,
            genres = genreNames,
            createdDate = entity.createdDate,
            updatedDate = entity.updatedDate
        )
    }
}

