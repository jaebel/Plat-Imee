package com.platimee.spring_platimee.entrypointTests.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class UpdateUserControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
    val userRepository: UserRepository
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    // Happy paths

    test("Can update existing user") {
        val testUser = UserCreateDTO("UserToUpdate", "UserToUpdate@gmail.com", "First", "Last", "TestPassword1&")
        val result = mvc.createUser(objectMapper, testUser)
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val responseContent = response.contentAsString
        val responseAsUser = objectMapper.readValue(responseContent, UserResponseDTO::class.java)

        val updatedUser = UserUpdateDTO("UserUpdatedName", "UpdatedUser@gmail.com", "UpdatedFirst", "UpdatedLast", "TestPassword2&")
        val updateResult = mvc.updateUser(objectMapper, updatedUser, responseAsUser.userId)

        updateResult.response.status shouldBe HttpStatus.OK.value()
        val updatedResponse = objectMapper.readValue(updateResult.response.contentAsString, UserResponseDTO::class.java)

        updatedResponse.userId shouldBe responseAsUser.userId
        updatedResponse.username shouldBe "UserUpdatedName"
        updatedResponse.firstName shouldBe "UpdatedFirst"
        updatedResponse.lastName shouldBe "UpdatedLast"

        val updatedUserEntity = userRepository.findById(responseAsUser.userId).get()
        BCryptPasswordEncoder().matches("TestPassword2&", updatedUserEntity.password) shouldBe true
    }

    test("Can partially update user fields") {
        val testUser = UserCreateDTO("PartialUser", "partialuser@gmail.com", "First", "Last", "PartialPassword1&")
        val result = mvc.createUser(objectMapper, testUser)
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val responseAsUser = objectMapper.readValue(response.contentAsString, UserResponseDTO::class.java)

        val partialUpdate = UserUpdateDTO(username = "UpdatedPartialUser", firstName = "UpdatedFirst")
        val updateResult = mvc.updateUser(objectMapper, partialUpdate, responseAsUser.userId)

        updateResult.response.status shouldBe HttpStatus.OK.value()
        val updatedResponse = objectMapper.readValue(updateResult.response.contentAsString, UserResponseDTO::class.java)

        updatedResponse.username shouldBe "UpdatedPartialUser"
        updatedResponse.firstName shouldBe "UpdatedFirst"

        // Should remain unchanged
        updatedResponse.lastName shouldBe "Last"
        updatedResponse.email shouldBe "partialuser@gmail.com"

        val updatedUserEntity = userRepository.findById(responseAsUser.userId).get()
        BCryptPasswordEncoder().matches("PartialPassword1&", updatedUserEntity.password) shouldBe true
    }

    // Sad paths

    test("Updating a non-existent user should return NOT FOUND") {
        val nonExistentUserId = 9999L
        val updateRequest = UserUpdateDTO(username = "FakeUser", email = "fake@gmail.com")

        val updateResult = mvc.updateUser(objectMapper, updateRequest, nonExistentUserId)

        updateResult.response.status shouldBe HttpStatus.NOT_FOUND.value()
        updateResult.response.contentAsString shouldContain "User with ID $nonExistentUserId not found"
    }

    test("Updating user with invalid email should return BAD REQUEST") {
        val testUser = UserCreateDTO("UserToUpdate", "UserToUpdate@gmail.com", "First", "Last", "TestPassword1&")
        val result = mvc.createUser(objectMapper, testUser)
        val responseAsUser = objectMapper.readValue(result.response.contentAsString, UserResponseDTO::class.java)

        val invalidEmailUpdate = UserUpdateDTO(email = "not-an-email")
        val updateResult = mvc.updateUser(objectMapper, invalidEmailUpdate, responseAsUser.userId)

        updateResult.response.status shouldBe HttpStatus.BAD_REQUEST.value()
        updateResult.response.contentAsString shouldContain "Email must be a valid format"
    }

    test("Updating user with an invalid password should return BAD REQUEST") {
        // Given: Create a new user
        val testUser = UserCreateDTO("UserToUpdateBadPassword", "UserToUpdateBadPassword@gmail.com", "First", "Last", "TestPassword1&")
        val result = mvc.createUser(objectMapper, testUser)
        val response = result.response
        response.status shouldBe HttpStatus.CREATED.value()

        val responseAsUser = objectMapper.readValue(response.contentAsString, UserResponseDTO::class.java)

        val invalidUpdate = UserUpdateDTO(password = "Invalid123") // Missing special character
        val updateResult = mvc.updateUser(objectMapper, invalidUpdate, responseAsUser.userId)

        updateResult.response.status shouldBe HttpStatus.BAD_REQUEST.value()
        updateResult.response.contentAsString shouldContain "Password must contain"
    }

})