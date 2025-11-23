package com.platimee.spring_platimee.anime.service

import com.platimee.spring_platimee.anime.model.AnimeDtoMapper
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.AnimeUpdateDTO
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateAnimeService(
    private val animeRepository: AnimeRepository,
    private val genreRepository: GenreRepository
) {

    @Transactional
    fun updateAnime(animeId: Long, updateDTO: AnimeUpdateDTO): AnimeResponseDTO {
        val anime = animeRepository.findById(animeId)
            .orElseThrow { EntityNotFoundException("Anime with id $animeId not found.") }

        // Use the mapper to update the entity with fields from the DTO.
        AnimeDtoMapper.updateEntityFromDto(anime, updateDTO)

        // Update genres separately if provided.
        updateDTO.genres?.let { genreNames ->
            val newGenres = genreRepository.findByNameIn(genreNames)
            anime.genres.clear()
            anime.genres.addAll(newGenres)
        }

        val updatedAnime = animeRepository.save(anime)
        return AnimeDtoMapper.toResponseDto(updatedAnime)
    }
}