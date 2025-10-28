package com.platimee.spring_platimee.entrypointTests.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserResponseDTO
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class GetUserControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    // Happy paths

    test("Can retrieve all users") {
        val testUser1 = UserCreateDTO("UserOne", "userone@gmail.com", "First", "Last", "TestPassword1&")
        val testUser2 = UserCreateDTO("UserTwo", "usertwo@gmail.com", "First", "Last", "TestPassword2&")

        mvc.createUser(objectMapper, testUser1)
        mvc.createUser(objectMapper, testUser2)

        val result = mvc.getAllUsers()

        result.response.status shouldBe HttpStatus.OK.value()

        val responseList = objectMapper.readValue(result.response.contentAsString, Array<UserResponseDTO>::class.java)
        responseList.size shouldBe 2
        responseList.map { it.username } shouldBe listOf("UserOne", "UserTwo")
    }

    test("Can retrieve a user by ID") {
        val testUser = UserCreateDTO("SingleUser", "singleuser@gmail.com", "First", "Last", "TestPassword1&")
        val result = mvc.createUser(objectMapper, testUser)

        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val createdUser = objectMapper.readValue(response.contentAsString, UserResponseDTO::class.java)

        val getResult = mvc.getUser(createdUser.userId)

        getResult.response.status shouldBe HttpStatus.OK.value()
        val fetchedUser = objectMapper.readValue(getResult.response.contentAsString, UserResponseDTO::class.java)

        fetchedUser.userId shouldBe createdUser.userId
        fetchedUser.username shouldBe "SingleUser"
        fetchedUser.email shouldBe "singleuser@gmail.com"
        fetchedUser.firstName shouldBe "First"
        fetchedUser.lastName shouldBe "Last"
    }

    // Sad path

    test("Retrieving a non-existent user should return NOT FOUND") {
        val invalidUserId = 9999L

        val result = mvc.getUser(invalidUserId)

        result.response.status shouldBe HttpStatus.NOT_FOUND.value()
        result.response.contentAsString shouldContain "User with ID $invalidUserId not found"
    }

})

// TODO: Add tests for authenticated users