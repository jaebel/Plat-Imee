package com.platimee.spring_platimee.entrypointTests.anime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.*
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import org.slf4j.LoggerFactory
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class UpdateAnimeControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
    val animeRepository: AnimeRepository,
    val genreRepository: GenreRepository
) : IntegrationTestSpec({

    val logger = LoggerFactory.getLogger(UpdateAnimeControllerTests::class.java)

    beforeEach {
        clearAllMocks()

        genreRepository.save(Genre(name = "Action"))
        genreRepository.save(Genre(name = "Adventure"))

        logger.info("Genres added: {}", genreRepository.findAll())
    }

    // Happy path test

    test("Can update existing anime") {
        // Given
        val testAnime = AnimeCreateDTO(
            malId = 1,
            name = "Naruto",
            type = AnimeType.TV,
            episodes = 220,
            score = 8.5,
            members = 1000000,
            genres = listOf(1, 2)
        )

        val result = mvc.createAnime(objectMapper, testAnime)
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val responseContent = response.contentAsString
        val responseAsAnime = objectMapper.readValue(responseContent, AnimeResponseDTO::class.java)

        responseAsAnime.genres.sorted() shouldBe listOf("Action", "Adventure").sorted()

        val updatedAnime = AnimeUpdateDTO(
            name = "Updated Anime",
            episodes = 24,
            score = 8.8,
            members = 10000,
            genres = listOf(1)
        )

        val updateResult = mvc.updateAnime(objectMapper, updatedAnime, responseAsAnime.malId)

        updateResult.response.status shouldBe HttpStatus.OK.value()

        val updatedResponse = objectMapper.readValue(updateResult.response.contentAsString, AnimeResponseDTO::class.java)

        updatedResponse.malId shouldBe responseAsAnime.malId
        updatedResponse.name shouldBe "Updated Anime"
        updatedResponse.episodes shouldBe 24
        updatedResponse.score shouldBe 8.8
        updatedResponse.genres shouldBe listOf("Action")
        responseAsAnime.genres.sorted() shouldBe listOf("Action", "Adventure").sorted()

        // Checking the repo
        val updatedAnimeEntity = animeRepository.findById(responseAsAnime.malId).get()
        updatedAnimeEntity.name shouldBe "Updated Anime"
        updatedAnimeEntity.episodes shouldBe 24
        updatedAnimeEntity.score shouldBe 8.8
    }
})
