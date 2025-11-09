package com.platimee.spring_platimee.anime.service

import com.platimee.spring_platimee.anime.model.AnimeDtoMapper
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateAnimeService(
    private val animeRepository: AnimeRepository,
    private val genreRepository: GenreRepository
) {

    private val logger = LoggerFactory.getLogger(CreateAnimeService::class.java)

    @Transactional
    fun addAnime(animeCreateDTO: com.platimee.spring_platimee.anime.model.AnimeCreateDTO): AnimeResponseDTO {
        // Fetch Genre entities based on the provided IDs.
        val genres = genreRepository.findAllById(animeCreateDTO.genres.toSet())

        // Map DTO to entity.
        val anime = AnimeDtoMapper.toEntity(animeCreateDTO)

        // Set the genres on the Anime entity.
        anime.genres.addAll(genres)

        // Save the entity.
        val savedAnime = animeRepository.save(anime)
        logger.info("Anime created successfully: ${savedAnime.malId}")

        // Map to response DTO and return.
        return AnimeDtoMapper.toResponseDto(savedAnime)
    }
}

