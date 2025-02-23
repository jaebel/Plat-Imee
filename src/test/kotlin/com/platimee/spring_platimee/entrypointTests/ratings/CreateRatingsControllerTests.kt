package com.platimee.spring_platimee.entrypointTests.ratings

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.model.Genre
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import com.platimee.spring_platimee.ratings.repository.RatingRepository
import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.users.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class CreateRatingsControllerTests(
    val mvc: MockMvc,
    val objectMapper: ObjectMapper,
    val ratingRepository: RatingRepository,
    val userRepository: UserRepository,
    val animeRepository: AnimeRepository,
    val genreRepository: GenreRepository
) : IntegrationTestSpec({

    beforeEach {
        // Clear previous data in the repos before each test
        ratingRepository.deleteAll()
        userRepository.deleteAll()
        animeRepository.deleteAll()
        genreRepository.deleteAll()

        val fantasyGenre = genreRepository.save(Genre(name = "Fantasy"))

        println("GENRESSSSSS: ${genreRepository.findAll()}")

        // Create sample data (user and anime)
        userRepository.save(User(username = "TestUser", email = "TestUser@gmail.com", firstName = "Test", lastName = "User", password = "TestPassword1&"))

        animeRepository.save(Anime(name = "Naruto", type = AnimeType.TV, episodes = 220, rating = 8.5, members = 1000000, genres = mutableSetOf(fantasyGenre)))
    }

    test("Can create a new rating") {
        // Given: A valid rating DTO for a user and an anime
        val user = userRepository.findAll().first()
        val anime = animeRepository.findAll().first()

        val testRating = RatingCreateDTO(userId = user.userId!!, animeId = anime.animeId!!, rating = 8.0)
        val result = mvc.createRating(objectMapper, testRating)
        result.response.contentAsString
        val responseAsAnime = objectMapper.readValue(result.response.contentAsString, RatingResponseDTO::class.java)

        responseAsAnime.id shouldNotBe null
        responseAsAnime.userId shouldBe user.userId
        responseAsAnime.animeId shouldBe anime.animeId
        responseAsAnime.rating shouldBe 8.0

        // Also ensure the rating was saved in the database
        val savedRating = ratingRepository.findAll().first()
        savedRating.rating shouldBe 8.0
        savedRating.user.userId shouldBe user.userId
        savedRating.anime.animeId shouldBe anime.animeId
    }
})
