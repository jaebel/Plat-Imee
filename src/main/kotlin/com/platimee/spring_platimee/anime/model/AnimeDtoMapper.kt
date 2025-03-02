package com.platimee.spring_platimee.anime.model

import java.time.Instant

object AnimeDtoMapper {

    // Convert from AnimeCreateDTO to Anime entity.
    // Note: The genres list (of Long IDs) is not handled here.
    fun toEntity(dto: AnimeCreateDTO): Anime {
        return Anime(
            malId = dto.malId,
            name = dto.name,
            englishName = dto.englishName,
            japaneseName = dto.japaneseName,
            type = dto.type,
            episodes = dto.episodes,
            score = dto.score,         // Renamed from rating to score
            aired = dto.aired,
            premiered = dto.premiered,
            genres = mutableSetOf(),   // Will be set in the service layer by mapping dto.genres to Genre entities
            createdDate = Instant.now(),
            updatedDate = Instant.now()
        )
    }

    // Convert from Anime entity to AnimeResponseDTO.
    fun toResponseDto(entity: Anime): AnimeResponseDTO {
        val genreNames = entity.genres.map { it.name }
        return AnimeResponseDTO(
            animeId = entity.animeId!!,
            malId = entity.malId,
            name = entity.name,
            englishName = entity.englishName,
            japaneseName = entity.japaneseName,
            type = entity.type,
            episodes = entity.episodes,
            score = entity.score,      // Renamed field
            aired = entity.aired,
            premiered = entity.premiered,
            genres = genreNames,
            createdDate = entity.createdDate,
            updatedDate = entity.updatedDate
        )
    }

    // Update an existing Anime entity from AnimeUpdateDTO.
    fun updateEntityFromDto(entity: Anime, dto: AnimeUpdateDTO): Anime {
        dto.malId?.let { entity.malId = it }
        dto.name?.let { entity.name = it }
        dto.englishName?.let { entity.englishName = it }
        dto.japaneseName?.let { entity.japaneseName = it }
        dto.type?.let { entity.type = it }
        dto.episodes?.let { entity.episodes = it }
        dto.score?.let { entity.score = it }
        dto.aired?.let { entity.aired = it }
        dto.premiered?.let { entity.premiered = it }
        // For genres, update the relationship in your service if needed.
        return entity
    }
}
