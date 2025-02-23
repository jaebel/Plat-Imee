package com.platimee.spring_platimee.ratings.model

import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.users.model.User

object RatingDtoMapper {
    fun toResponseDto(rating: Rating): RatingResponseDTO {
        return RatingResponseDTO(
            id = rating.id!!,
            userId = rating.user.userId!!,
            animeId = rating.anime.animeId!!,
            rating = rating.rating,
            ratedAt = rating.ratedAt
        )
    }

    fun toEntity(dto: RatingCreateDTO, user: User, anime: Anime): Rating {
        return Rating(
            user = user,
            anime = anime,
            rating = dto.rating
        )
    }
}
