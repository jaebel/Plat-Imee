package com.platimee.spring_platimee.anime.service

import com.platimee.spring_platimee.anime.model.AnimeDtoMapper
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class GetAnimeService(private val animeRepository: AnimeRepository) {

    fun getAll(): List<AnimeResponseDTO> {
        val anime = animeRepository.findAll()
        if (anime.isEmpty()) {
            throw EntityNotFoundException("No anime found")
        }
        return anime.map { AnimeDtoMapper.toResponseDto(it) }
    }

    fun getById(malId: Long): AnimeResponseDTO {
        val anime = animeRepository.findByMalId(malId)
            ?: throw EntityNotFoundException("Anime with ID $malId not found")
        return AnimeDtoMapper.toResponseDto(anime)
    }
}