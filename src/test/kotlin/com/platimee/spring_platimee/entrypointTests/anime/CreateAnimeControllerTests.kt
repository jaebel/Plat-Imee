package com.platimee.spring_platimee.entrypointTests.anime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.model.Genre
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class CreateAnimeControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
    val genreRepository: GenreRepository
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()

        genreRepository.save(Genre(name = "Action"))
        genreRepository.save(Genre(name = "Adventure"))
    }

    // Happy path

    test("Can create anime") {
        // Given
        val testAnime = AnimeCreateDTO(
            name = "Naruto",
            type = AnimeType.TV,
            episodes = 220,
            score = 8.5,
            members = 1000000,
            genres = listOf(1, 2)
        )

        // When
        val result = mvc.createAnime(objectMapper, testAnime)

        // Then
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        // Debugging
        val responseContent = response.contentAsString
        println("Response Content: $responseContent")

        val responseAsAnime = objectMapper.readValue(responseContent, AnimeResponseDTO::class.java)

        responseAsAnime.name shouldBe testAnime.name
        responseAsAnime.type shouldBe testAnime.type
        responseAsAnime.episodes shouldBe testAnime.episodes
        responseAsAnime.score shouldBe testAnime.score
        responseAsAnime.genres shouldBe listOf("Action", "Adventure")
        responseAsAnime.animeId shouldBe 1
    }
})
