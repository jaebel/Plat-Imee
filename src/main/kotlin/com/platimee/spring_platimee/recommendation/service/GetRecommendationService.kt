package com.platimee.spring_platimee.recommendation.service

import com.platimee.spring_platimee.recommendation.model.AnimeEntryDTO
import com.platimee.spring_platimee.recommendation.model.RecResponseDTO
import com.platimee.spring_platimee.recommendation.model.UserAnimeListDTO
import com.platimee.spring_platimee.useranime.service.GetUserAnimeService
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GetRecommendationService(
    private val restTemplate: RestTemplate,
    private val getUserAnimeService: GetUserAnimeService
) {
    // Hardcoded URL for the recommendation service
    private val recommendationServiceUrl: String = "http://localhost:5000"

    fun getRecommendations(userId: Long): List<RecResponseDTO> {
        // Retrieve user's anime list from the database
        val userAnimeListDTOs = getUserAnimeService.getUserAnimeByUserId(userId)

        // Map each UserAnimeResponseDTO to AnimeEntryDTO, defaulting rating to 7.0 if missing
        val animeList = userAnimeListDTOs.map {
            AnimeEntryDTO(
                malId = it.malId,
                rating = it.rating ?: 7.0
            )
        }

        // Construct payload for recommendation system
        val payload = UserAnimeListDTO(
            userId = userId,
            animeList = animeList
        )

        // Call the python recommendation microservice with the payload
        val url = "$recommendationServiceUrl/api/recommendations"
        val response = restTemplate.postForEntity(url, payload, Array<RecResponseDTO>::class.java)
        return response.body?.toList() ?: emptyList()
    }
}
