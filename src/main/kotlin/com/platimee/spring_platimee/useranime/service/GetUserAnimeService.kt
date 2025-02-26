package com.platimee.spring_platimee.useranime.service

import com.platimee.spring_platimee.useranime.model.UserAnimeDtoMapper
import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.repository.UserAnimeRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserAnimeService(private val userAnimeRepository: UserAnimeRepository) {

    @Transactional(readOnly = true)
    fun getUserAnime(id: Long): UserAnimeResponseDTO {
        val userAnime = userAnimeRepository.findById(id)
            .orElseThrow { EntityNotFoundException("UserAnime record with ID $id not found") }
        return UserAnimeDtoMapper.toResponseDTO(userAnime)
    }
}
