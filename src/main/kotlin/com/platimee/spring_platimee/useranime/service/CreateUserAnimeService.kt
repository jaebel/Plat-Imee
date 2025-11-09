package com.platimee.spring_platimee.useranime.service

import com.fasterxml.jackson.databind.JsonNode
import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.useranime.model.UserAnimeCreateDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeDtoMapper
import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.repository.UserAnimeRepository
import com.platimee.spring_platimee.users.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class CreateUserAnimeService(
    private val userAnimeRepository: UserAnimeRepository,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository,
    private val genreRepository: GenreRepository
) {

    private val restTemplate = RestTemplate()

    @Transactional
    fun addUserAnime(dto: UserAnimeCreateDTO): UserAnimeResponseDTO {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { EntityNotFoundException("User with ID \${dto.userId} not found") }

        var anime = animeRepository.findById(dto.malId).orElse(null)

        if (anime == null) {
            val jikanResponse: JsonNode = restTemplate.getForObject("https://api.jikan.moe/v4/anime/${dto.malId}")

                ?: throw EntityNotFoundException("Anime with ID \${dto.malId} not found on Jikan")

            val data = jikanResponse["data"]
                ?: throw IllegalStateException("Malformed response from Jikan")

            val genreNames = data["genres"]?.mapNotNull { it["name"]?.asText() } ?: emptyList()
            val matchedGenres = genreRepository.findByNameIn(genreNames)

            val createdAnime = Anime(
                malId = dto.malId,
                name = data["title_english"]?.asText() ?: data["title"]?.asText() ?: "Unknown Title",
                type = mapToAnimeType(data["type"]?.asText()),
                episodes = data["episodes"]?.asInt() ?: 1,
                score = data["score"]?.asDouble() ?: 0.0,
                aired = data["aired"]?.get("string")?.asText() ?: "",
                premiered = data["season"]?.asText() ?: "",
                genres = matchedGenres.toMutableSet()
            )
            anime = animeRepository.save(createdAnime)
        }

        userAnimeRepository.findAll().find { it.user.userId == dto.userId && it.anime.malId == dto.malId }?.let {
            throw IllegalStateException("This anime is already in the user's list.")
        }

        val userAnime = UserAnimeDtoMapper.toEntity(dto, user, anime)
        val savedRecord = userAnimeRepository.save(userAnime)
        return UserAnimeDtoMapper.toResponseDTO(savedRecord)
    }

    private fun mapToAnimeType(typeStr: String?): com.platimee.spring_platimee.anime.model.AnimeType {
        return try {
            com.platimee.spring_platimee.anime.model.AnimeType.valueOf(typeStr?.uppercase() ?: "TV")
        } catch (e: IllegalArgumentException) {
            com.platimee.spring_platimee.anime.model.AnimeType.TV
        }
    }
}
