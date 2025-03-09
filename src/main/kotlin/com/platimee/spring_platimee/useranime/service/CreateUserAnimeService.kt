package com.platimee.spring_platimee.useranime.service

import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.useranime.model.*
import com.platimee.spring_platimee.useranime.repository.UserAnimeRepository
import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUserAnimeService(
    private val userAnimeRepository: UserAnimeRepository,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository
) {

    @Transactional
    fun addUserAnime(dto: UserAnimeCreateDTO): UserAnimeResponseDTO {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { EntityNotFoundException("User with ID ${dto.userId} not found") }
        val anime = animeRepository.findById(dto.animeId)
            .orElseThrow { EntityNotFoundException("Anime with ID ${dto.animeId} not found") }

        // Check if a record already exists
        userAnimeRepository.findAll().find { it.user.userId == dto.userId && it.anime.animeId == dto.animeId }?.let {
            throw IllegalStateException("This anime is already in the user's list.")
        }

        val userAnime = UserAnimeDtoMapper.toEntity(dto, user, anime)
        val savedRecord = userAnimeRepository.save(userAnime)
        return UserAnimeDtoMapper.toResponseDTO(savedRecord)
    }
}
