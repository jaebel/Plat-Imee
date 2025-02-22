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

        // Update fields only if provided
        updateDTO.name?.let { anime.name = it }
        updateDTO.type?.let { anime.type = it }
        updateDTO.episodes?.let { anime.episodes = it }
        updateDTO.rating?.let { anime.rating = it }
        updateDTO.members?.let { anime.members = it }

        // Update genres if provided
        updateDTO.genres?.let { genreIds ->
            val newGenres = genreRepository.findAllById(genreIds.toSet())
            anime.genres.clear()
            anime.genres.addAll(newGenres)
        }

        val updatedAnime = animeRepository.save(anime)
        return AnimeDtoMapper.toResponseDto(updatedAnime)
    }
}
