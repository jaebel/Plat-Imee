package com.platimee.spring_platimee.useranime.service

import com.platimee.spring_platimee.useranime.model.UserAnime
import com.platimee.spring_platimee.useranime.model.UserAnimeDtoMapper
import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeUpdateDTO
import com.platimee.spring_platimee.useranime.repository.UserAnimeRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateUserAnimeService(private val userAnimeRepository: UserAnimeRepository) {

    @Transactional
    fun updateUserAnime(id: Long, dto: UserAnimeUpdateDTO): UserAnimeResponseDTO {
        val userAnime: UserAnime = userAnimeRepository.findById(id)
            .orElseThrow { EntityNotFoundException("UserAnime record with ID $id not found") }

        // Update the entity from the DTO using the mapper
        UserAnimeDtoMapper.updateEntityFromDTO(userAnime, dto)

        val updatedRecord = userAnimeRepository.save(userAnime)
        return UserAnimeDtoMapper.toResponseDTO(updatedRecord)
    }
}
