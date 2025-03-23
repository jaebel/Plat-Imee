package com.platimee.spring_platimee.recommendation.service

import com.platimee.spring_platimee.recommendation.model.AnimeEntryDTO
import com.platimee.spring_platimee.recommendation.model.RecResponseDTO
import com.platimee.spring_platimee.recommendation.model.UserAnimeListDTO
import com.platimee.spring_platimee.useranime.service.GetUserAnimeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GetRecommendationService(
    private val restTemplate: RestTemplate,
    private val getUserAnimeService: GetUserAnimeService
) {

    private val logger = LoggerFactory.getLogger(GetRecommendationService::class.java)
    // Hardcoded URL for the recommendation service
    private val recommendationServiceUrl: String = "http://localhost:5000"

    fun getRecommendations(userId: Long): List<RecResponseDTO> {
        logger.info("Starting to get recommendations for user: {}", userId)

        // Retrieve user's anime list from the database
        val userAnimeListDTOs = getUserAnimeService.getUserAnimeByUserId(userId)
        logger.info("Retrieved {} anime records for user {}", userAnimeListDTOs.size, userId)

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
        logger.debug("Constructed payload for recommendation service: {}", payload)

        // Call the python recommendation microservice with the payload
        val url = "$recommendationServiceUrl/api/recommendations"
        logger.info("Sending POST request to external recommendation service at {}", url)
        val response = restTemplate.postForEntity(url, payload, Array<RecResponseDTO>::class.java)

        if (response.statusCode.is2xxSuccessful) {
            logger.info("Successfully received response from recommendation service")
        } else {
            logger.error("Failed to receive a proper response. Status: {}", response.statusCode)
        }

        val recList = response.body?.toList() ?: emptyList()
        logger.info("Returning {} recommendations for user {}", recList.size, userId)
        return recList
    }
}
