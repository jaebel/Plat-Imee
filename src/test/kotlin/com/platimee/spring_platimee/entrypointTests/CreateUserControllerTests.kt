package com.platimee.spring_platimee.entrypointTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.model.UserCreateDTO
import com.platimee.spring_platimee.model.UserResponseDTO
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class CreateUserControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
) : IntegrationTestSpec({

    test("Can create user") {
        // Given
        val testUser = UserCreateDTO("TestUser", "TestUser@gmail.com", "Test", "User", "TestPassword")

        // When
        val result = mvc.createUser(objectMapper, testUser)

        // Then
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        // Debugging
        val responseContent = response.contentAsString
        println("Response Content: $responseContent")

        // Deserialize the response content to a UserResponseDTO
        val responseAsUser = objectMapper.readValue(responseContent, UserResponseDTO::class.java)

        responseAsUser.username shouldBe testUser.username
        responseAsUser.email shouldBe testUser.email
        responseAsUser.firstName shouldBe testUser.firstName
        responseAsUser.lastName shouldBe testUser.lastName
        responseAsUser.userId shouldBe 1
    }

})