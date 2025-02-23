package com.platimee.spring_platimee.ratings.service

import com.platimee.spring_platimee.ratings.model.RatingDtoMapper
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import com.platimee.spring_platimee.ratings.repository.RatingRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UpdateRatingService(
    private val ratingRepository: RatingRepository
) {

    @Transactional
    fun updateRating(ratingId: Long, newRating: Double): RatingResponseDTO {
        val rating = ratingRepository.findById(ratingId)
            .orElseThrow { EntityNotFoundException("Rating with id $ratingId not found.") }

        rating.rating = newRating
        rating.ratedAt = Instant.now()

        val updatedRating = ratingRepository.save(rating)
        return RatingDtoMapper.toResponseDto(updatedRating)
    }
}
