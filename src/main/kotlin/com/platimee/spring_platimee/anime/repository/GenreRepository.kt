package com.platimee.spring_platimee.anime.repository

import com.platimee.spring_platimee.anime.model.Genre
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<Genre, Long> {
    fun findByName(genreName: String): Genre?
    fun findByNameIn(names: List<String>): List<Genre>
}
