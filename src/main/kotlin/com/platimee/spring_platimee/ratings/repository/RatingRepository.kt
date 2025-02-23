package com.platimee.spring_platimee.ratings.repository

import com.platimee.spring_platimee.ratings.model.Rating
import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.anime.model.Anime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : JpaRepository<Rating, Long> {
    fun findByUserAndAnime(user: User, anime: Anime): Rating?
}
