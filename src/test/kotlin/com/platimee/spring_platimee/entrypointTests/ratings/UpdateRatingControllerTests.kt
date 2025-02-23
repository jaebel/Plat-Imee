package com.platimee.spring_platimee.entrypointTests.ratings

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.Anime
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.model.Genre
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.anime.repository.GenreRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.ratings.model.RatingUpdateDTO
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import com.platimee.spring_platimee.ratings.repository.RatingRepository
import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.users.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class UpdateRatingControllerTests(
    val mvc: MockMvc,
    val objectMapper: ObjectMapper,
    val ratingRepository: RatingRepository,
    val userRepository: UserRepository,
    val animeRepository: AnimeRepository,
    val genreRepository: GenreRepository
) : IntegrationTestSpec({

    beforeEach {
        ratingRepository.deleteAll()
        userRepository.deleteAll()
        animeRepository.deleteAll()
        genreRepository.deleteAll()

        val fantasyGenre = genreRepository.save(Genre(name = "Fantasy"))

        val user = userRepository.save(User(username = "TestUser", email = "TestUser@gmail.com", firstName = "Test", lastName = "User", password = "TestPassword1&"))

        val anime = animeRepository.save(Anime(name = "Naruto", type = AnimeType.TV, episodes = 220, rating = 8.5, members = 1000000, genres = mutableSetOf(fantasyGenre)))

        val initialRating = RatingCreateDTO(userId = user.userId!!, animeId = anime.animeId!!, rating = 7.5)
        val result = mvc.createRating(objectMapper, initialRating)
        val createdRating = objectMapper.readValue(result.response.contentAsString, RatingResponseDTO::class.java)

        createdRating.id shouldNotBe null
    }

    test("Can update an existing rating") {
        val user = userRepository.findAll().first()
        val anime = animeRepository.findAll().first()
        val rating = ratingRepository.findAll().first()

        val updatedRating = RatingUpdateDTO(rating = 9.0)

        val result = mvc.updateRating(objectMapper, updatedRating, rating.id!!)

        result.response.status shouldBe HttpStatus.OK.value()

        val responseAsRating = objectMapper.readValue(result.response.contentAsString, RatingResponseDTO::class.java)

        responseAsRating.id shouldBe rating.id
        responseAsRating.userId shouldBe user.userId
        responseAsRating.animeId shouldBe anime.animeId
        responseAsRating.rating shouldBe 9.0

        val savedRating = ratingRepository.findById(rating.id!!).get()
        savedRating.rating shouldBe 9.0
    }
})
