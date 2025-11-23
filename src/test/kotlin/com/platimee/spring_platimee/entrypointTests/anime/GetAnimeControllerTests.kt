package com.platimee.spring_platimee.entrypointTests.anime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.model.Genre
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class GetAnimeControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
    val genreRepository: GenreRepository
) : IntegrationTestSpec({

    genreRepository.save(Genre(name = "Action"))
    genreRepository.save(Genre(name = "Adventure"))

    beforeEach {
        clearAllMocks()
    }

    // Happy paths

    test("Can retrieve all anime") {
        val testAnime1 = AnimeCreateDTO(
            malId = 1,
            name = "Naruto",
            type = AnimeType.TV,
            episodes = 220,
            score = 8.5,
            members = 1000000,
            genres = listOf("Action", "Adventure")
        )
        val testAnime2 = AnimeCreateDTO(
            malId = 2,
            name = "Sousou no Frieren",
            type = AnimeType.TV,
            episodes = 28,
            score = 8.5,
            members = 1000000,
            genres = listOf("Action", "Adventure")
        )

        val result1 = mvc.createAnime(objectMapper, testAnime1).response
        val result2 = mvc.createAnime(objectMapper, testAnime2).response

        result1.status shouldBe HttpStatus.CREATED.value()
        result2.status shouldBe HttpStatus.CREATED.value()

        val result = mvc.getAllAnime()

        result.response.status shouldBe HttpStatus.OK.value()

        val responseList = objectMapper.readValue(result.response.contentAsString, Array<AnimeResponseDTO>::class.java)
        responseList.size shouldBe 2
        responseList.map { it.name } shouldBe listOf("Naruto", "Sousou no Frieren")
    }

    test("Can retrieve a anime by ID") {
        val testAnime = AnimeCreateDTO(
            malId = 1,
            name = "Naruto",
            type = AnimeType.TV,
            episodes = 220,
            score = 8.5,
            members = 1000000,
            genres = listOf("Action", "Adventure")
        )
        val result = mvc.createAnime(objectMapper, testAnime)

        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val createdAnime = objectMapper.readValue(response.contentAsString, AnimeResponseDTO::class.java)

        val getResult = mvc.getAnime(createdAnime.malId)

        getResult.response.status shouldBe HttpStatus.OK.value()
        val fetchedAnime = objectMapper.readValue(getResult.response.contentAsString, AnimeResponseDTO::class.java)

        fetchedAnime.malId shouldBe createdAnime.malId
        fetchedAnime.name shouldBe "Naruto"
        fetchedAnime.type shouldBe AnimeType.TV
        fetchedAnime.episodes shouldBe 220
        fetchedAnime.score shouldBe 8.5
        fetchedAnime.genres.sorted() shouldBe listOf("Action", "Adventure").sorted()
    }

    // Sad path

    test("Retrieving a non-existent anime should return NOT FOUND") {
        val invalidAnimeId = 9999L

        val result = mvc.getAnime(invalidAnimeId)

        result.response.status shouldBe HttpStatus.NOT_FOUND.value()
        result.response.contentAsString shouldContain "Anime with ID $invalidAnimeId not found"
    }

})