package com.platimee.spring_platimee.entrypointTests.useranime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeDtoMapper
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.AnimeType
import com.platimee.spring_platimee.anime.repository.AnimeRepository
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.useranime.model.UserAnimeCreateDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeStatus
import com.platimee.spring_platimee.useranime.model.UserAnimeUpdateDTO
import com.platimee.spring_platimee.users.model.User
import com.platimee.spring_platimee.users.model.UserDtoMapper
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class UserAnimeControllerTests(
    val mvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository,
    val animeRepository: AnimeRepository
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    // Create a user directly via repository
    fun createTestUserDirectly(): UserResponseDTO {
        val user = User(
            userId = null,
            username = "TestUser",
            email = "testuser@example.com",
            password = "TestPassword1&", // In tests, encoding might be handled separately.
            firstName = "Test",
            lastName = "User"
        )
        val savedUser = userRepository.save(user)
        return UserDtoMapper.toResponseDto(savedUser)
    }

    // Create an anime directly via repository
    fun createTestAnimeDirectly(): AnimeResponseDTO {
        val animeCreateDTO = AnimeCreateDTO(
            malId = 1,
            name = "Test Anime",
            type = AnimeType.TV,
            episodes = 24,
            score = 8.5,
            members = 10000,
            genres = listOf(1L) // Assumes a genre with ID 1 exists in test DB
        )
        val animeEntity = AnimeDtoMapper.toEntity(animeCreateDTO)
        val savedAnime = animeRepository.save(animeEntity)
        return AnimeDtoMapper.toResponseDto(savedAnime)
    }

    test("Can create a UserAnime record with default status WATCHING") {
        val user = createTestUserDirectly()
        val anime = createTestAnimeDirectly()

        val createDto = UserAnimeCreateDTO(
            userId = user.userId,
            malId = anime.malId,
            status = null // Should default to WATCHING
        )

        val result = mvc.createUserAnime(objectMapper, createDto)
        result.response.status shouldBe HttpStatus.CREATED.value()
        val response = objectMapper.readValue(result.response.contentAsString, UserAnimeResponseDTO::class.java)
        response.status shouldBe UserAnimeStatus.WATCHING
        response.userId shouldBe user.userId
        response.malId shouldBe anime.malId
    }

    test("Can retrieve an existing UserAnime record") {
        val user = createTestUserDirectly()
        val anime = createTestAnimeDirectly()

        val createDto = UserAnimeCreateDTO(
            userId = user.userId,
            malId = anime.malId,
            status = null // Should default to WATCHING
        )
        val createResult = mvc.createUserAnime(objectMapper, createDto)
        createResult.response.status shouldBe HttpStatus.CREATED.value()
        val createdRecord = objectMapper.readValue(createResult.response.contentAsString, UserAnimeResponseDTO::class.java)

        val getResult = mvc.getUserAnime(createdRecord.id)
        getResult.response.status shouldBe HttpStatus.OK.value()

        val fetchedRecord = objectMapper.readValue(getResult.response.contentAsString, UserAnimeResponseDTO::class.java)

        fetchedRecord.id shouldBe createdRecord.id
        fetchedRecord.userId shouldBe user.userId
        fetchedRecord.malId shouldBe anime.malId
        fetchedRecord.status shouldBe createdRecord.status
    }


    test("Can update an existing UserAnime record") {
        val user = createTestUserDirectly()
        val anime = createTestAnimeDirectly()

        // Create initial record
        val createDto = UserAnimeCreateDTO(
            userId = user.userId,
            malId = anime.malId,
            status = null
        )
        val createResult = mvc.createUserAnime(objectMapper, createDto)
        createResult.response.status shouldBe HttpStatus.CREATED.value()
        val createdRecord = objectMapper.readValue(createResult.response.contentAsString, UserAnimeResponseDTO::class.java)

        // Update the record
        val updateDto = UserAnimeUpdateDTO(
            status = UserAnimeStatus.COMPLETED,
            rating = 9.0,
            episodesWatched = 24
        )
        val updateResult = mvc.updateUserAnime(objectMapper, updateDto, createdRecord.id)
        updateResult.response.status shouldBe HttpStatus.OK.value()
        val updatedRecord = objectMapper.readValue(updateResult.response.contentAsString, UserAnimeResponseDTO::class.java)
        updatedRecord.status shouldBe UserAnimeStatus.COMPLETED
        updatedRecord.rating shouldBe 9.0
        updatedRecord.episodesWatched shouldBe 24
    }

    test("Deleting a UserAnime record returns NO_CONTENT and subsequent GET returns NOT_FOUND") {
        val user = createTestUserDirectly()
        val anime = createTestAnimeDirectly()

        val createDto = UserAnimeCreateDTO(
            userId = user.userId,
            malId = anime.malId,
            status = null
        )
        val createResult = mvc.createUserAnime(objectMapper, createDto)
        createResult.response.status shouldBe HttpStatus.CREATED.value()
        val createdRecord = objectMapper.readValue(createResult.response.contentAsString, UserAnimeResponseDTO::class.java)

        val deleteResult = mvc.deleteUserAnime(createdRecord.id)
        deleteResult.response.status shouldBe HttpStatus.NO_CONTENT.value()

        val getResult = mvc.getUserAnime(createdRecord.id)
        getResult.response.status shouldBe HttpStatus.NOT_FOUND.value()
        getResult.response.contentAsString shouldContain "not found"
    }

    test("Updating a non-existent UserAnime record returns NOT_FOUND") {
        val updateDto = UserAnimeUpdateDTO(
            status = UserAnimeStatus.DROPPED,
            rating = 5.0,
            episodesWatched = 10
        )
        val nonExistentId = 9999L
        val updateResult = mvc.updateUserAnime(objectMapper, updateDto, nonExistentId)
        updateResult.response.status shouldBe HttpStatus.NOT_FOUND.value()
        updateResult.response.contentAsString shouldContain "not found"
    }
})
