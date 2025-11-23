package com.platimee.spring_platimee.anime.service

import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeDtoMapper
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.Genre
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
    fun addAnime(animeCreateDTO: AnimeCreateDTO): AnimeResponseDTO {

        // Fetch genres that already exist
        var genres = genreRepository.findByNameIn(animeCreateDTO.genres)

        // Find which names were NOT returned
        val existingNames = genres.map { it.name }.toSet()
        val missingNames = animeCreateDTO.genres.filter { it !in existingNames }

        // Create genres for missing names
        if (missingNames.isNotEmpty()) {
            val newGenres = missingNames.map { Genre(name = it) }
            genres += genreRepository.saveAll(newGenres)
        }

        // Map DTO → entity
        val anime = AnimeDtoMapper.toEntity(animeCreateDTO)

        // Attach all genres (existing + newly created)
        anime.genres.addAll(genres)

        // Save
        val savedAnime = animeRepository.save(anime)
        logger.info("Anime created successfully: ${savedAnime.malId}")

        // Map to response DTO and return.
        return AnimeDtoMapper.toResponseDto(savedAnime)
    }
}

