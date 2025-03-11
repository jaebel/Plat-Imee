package com.platimee.spring_platimee.anime.repository

import com.platimee.spring_platimee.anime.model.Anime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimeRepository : JpaRepository<Anime, Long> {
    fun findByMalId(malId: Long): Anime?
}
