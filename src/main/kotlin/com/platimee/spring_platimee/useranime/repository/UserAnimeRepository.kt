package com.platimee.spring_platimee.useranime.repository

import com.platimee.spring_platimee.useranime.model.UserAnime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAnimeRepository : JpaRepository<UserAnime, Long>
