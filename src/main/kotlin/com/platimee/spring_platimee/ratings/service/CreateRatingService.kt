package com.platimee.spring_platimee.ratings.service

import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import com.platimee.spring_platimee.ratings.repository.RatingRepository
import com.platimee.spring_platimee.users.repository.UserRepository
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.ratings.model.RatingDtoMapper
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateRatingService(
    private val ratingRepository: RatingRepository,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository
) {

    @Transactional
    fun addRating(dto: RatingCreateDTO): RatingResponseDTO {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { EntityNotFoundException("User with id ${dto.userId} not found.") }

        val anime = animeRepository.findById(dto.animeId)
            .orElseThrow { EntityNotFoundException("Anime with id ${dto.animeId} not found.") }

        val rating = RatingDtoMapper.toEntity(dto, user, anime)
        val savedRating = ratingRepository.save(rating)

        return RatingDtoMapper.toResponseDto(savedRating)
    }
}
