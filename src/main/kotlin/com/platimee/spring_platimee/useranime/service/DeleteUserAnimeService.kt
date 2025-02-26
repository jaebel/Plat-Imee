package com.platimee.spring_platimee.useranime.service

import com.platimee.spring_platimee.useranime.repository.UserAnimeRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteUserAnimeService(private val userAnimeRepository: UserAnimeRepository) {

    @Transactional
    fun deleteUserAnime(id: Long) {
        if (!userAnimeRepository.existsById(id)) {
            throw EntityNotFoundException("UserAnime record with ID $id not found")
        }
        userAnimeRepository.deleteById(id)
    }
}
