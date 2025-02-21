package com.platimee.spring_platimee.entrypointTests.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserResponseDTO
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class DeleteUserControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
    val env: Environment // Injects Spring environment properties
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    // Happy path

    test("Can delete existing user") {
        // Given: Create a new user
        val testUser = UserCreateDTO("UserToDelete", "UserToDelete@gmail.com", "Test", "User", "TestPassword1&")
        val testUser2 = UserCreateDTO("TestUser", "TestUser@gmail.com", "Test", "User", "TestPassword1&")

        // Creating the user
        val result = mvc.createUser(objectMapper, testUser)
        val response = result.response
        result.response.status shouldBe HttpStatus.CREATED.value()
        val responseContent = response.contentAsString
        val responseAsUser = objectMapper.readValue(responseContent, UserResponseDTO::class.java)

        // Delete the user using the userId from the created user
        val deleteResult = mvc.deleteUser(responseAsUser.userId)
        deleteResult.response.status shouldBe HttpStatus.NO_CONTENT.value()

        // Try deleting them again
        val deleteAgainResult = mvc.deleteUser(responseAsUser.userId)
        deleteAgainResult.response.status shouldBe HttpStatus.NOT_FOUND.value()
        deleteAgainResult.response.contentAsString shouldContain "User with ID ${responseAsUser.userId} not found"
    }

    // Sad paths

    test("Deleting a non-existent user returns not found") {
        val nonExistentUserId = 9999L

        val result = mvc.deleteUser(nonExistentUserId)

        result.response.status shouldBe HttpStatus.NOT_FOUND.value()
        result.response.contentAsString shouldContain "User with ID $nonExistentUserId not found"
    }

    // Just a general test
    test("Log active profile and database URL") {
        val activeProfiles = env.activeProfiles.joinToString()
        val datasourceUrl = env.getProperty("spring.datasource.url")

        println("Active Profiles: $activeProfiles")
        println("Database URL: $datasourceUrl")

        activeProfiles shouldContain "test"
        datasourceUrl shouldContain "jdbc:h2:mem:testdb"
    }
})
