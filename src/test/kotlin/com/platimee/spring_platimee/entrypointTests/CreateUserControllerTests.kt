package com.platimee.spring_platimee.entrypointTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.model.UserCreateDTO
import com.platimee.spring_platimee.model.UserResponseDTO
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
class CreateUserControllerTests(
    val mvc: MockMvc,
    objectMapper: ObjectMapper,
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    // Happy path

    test("Can create user") {
        // Given
        val testUser = UserCreateDTO("TestUser", "TestUser@gmail.com", "Test", "User", "TestPassword1&")

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

    // Sad paths

    test("Cannot create user with already registered username") {
        val testUser1 = UserCreateDTO("TestUser1", "TestUser1@gmail.com", "Test1", "User1", "TestPassword1&")
        val testUser2 = UserCreateDTO("TestUser1", "TestUser2@gmail.com", "Test2", "User2", "TestPassword2&")

        mvc.createUser(objectMapper, testUser1)

        val result = mvc.createUser(objectMapper, testUser2)
        result.response.status shouldBe HttpStatus.CONFLICT.value()
    }

    test("Cannot create user with already registered email") {
        val testUser1 = UserCreateDTO("TestUser1", "TestUser1@gmail.com", "Test1", "User1", "TestPassword1&")
        val testUser2 = UserCreateDTO("TestUser2", "TestUser1@gmail.com", "Test2", "User2", "TestPassword2&")

        mvc.createUser(objectMapper, testUser1)

        val result = mvc.createUser(objectMapper, testUser2)
        result.response.status shouldBe HttpStatus.CONFLICT.value()
    }

    test("Cannot create user with missing fields") {
        val testCases = listOf(
            // Each case is a UserCreateDTO and its expected error message
            UserCreateDTO("", "user@example.com", "Test", "User", "ValidPass123!") to
                    "Username must be",

            UserCreateDTO("TestUser", "", "Test", "User", "ValidPass123!") to
                    "Email must be a valid format",

            UserCreateDTO("TestUser", "user@example.com", "", "User", "ValidPass123!") to
                    "First name must be",

            UserCreateDTO("TestUser", "user@example.com", "Test", "", "ValidPass123!") to
                    "Last name must be",

            UserCreateDTO("TestUser", "user@example.com", "Test", "User", "") to
                    "Password must contain"
        )

        testCases.forEach { (invalidUser, expectedMessage) ->
            val result = mvc.createUser(objectMapper, invalidUser)

            // Assert status is BAD_REQUEST
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()

            // Get the response as a string
            val responseContent = result.response.contentAsString

            // Assert that the specific error message is in the response
            responseContent shouldContain expectedMessage
        }
    }

    test("Cannot create user with invalid username, first name, or last name length. Or unexpected spaces") {
        // Invalid values and their corresponding error messages for each field
        val usernameErrorMessage = "Username must be between 2 and 50 characters."
        val emailErrorMessage = "Email must be a valid format"
        val firstNameErrorMessage = "First name must be between 2 and 50 characters."
        val lastNameErrorMessage = "Last name must be between 2 and 50 characters."
        val passwordErrorMessage = "Password must contain at least"

        val usernameInvalidValues = listOf(
            "A" to usernameErrorMessage,
            "A".repeat(51) to usernameErrorMessage,
            "     " to usernameErrorMessage, // caught by NotBlank but not by Size validation. These fields do not have regex constricting them
            "" to usernameErrorMessage,
            // TODO Add some validation and checks for spaces before and after
        )

        val emailInvalidValues = listOf(
            "invalidemail.com" to emailErrorMessage, // Missing '@' symbol
            "invalid@.com" to emailErrorMessage, // Missing domain part after '@'
            "invalid@domain" to emailErrorMessage, // Missing top-level domain
            "invalid@@domain.com" to emailErrorMessage, // Multiple '@' symbols
            "invalid@domain#$.com" to emailErrorMessage, // Invalid characters in the email
            "inva lid@domain#$.com" to emailErrorMessage, // Spaces in the email
            "" to emailErrorMessage,
        )

        val firstNameInvalidValues = listOf(
            "A" to firstNameErrorMessage,
            "A".repeat(51) to firstNameErrorMessage,
            "     " to firstNameErrorMessage,
            "" to firstNameErrorMessage,
        )

        val lastNameInvalidValues = listOf(
            "A" to lastNameErrorMessage,
            "A".repeat(51) to lastNameErrorMessage,
            "     " to lastNameErrorMessage,
            "" to lastNameErrorMessage,
        )

        val passwordInvalidValues = listOf(
            "NoDigit@" to passwordErrorMessage,    // Missing digit
            "NoSpecial1" to passwordErrorMessage,  // Missing special character
            "12345678@" to passwordErrorMessage,   // Missing letter
            "@@$$%%!!" to passwordErrorMessage,    // Only special characters
            "1234567890" to passwordErrorMessage,  // Only digits
            "Abcdefgh" to passwordErrorMessage,    // Only letters
            "Spaced Password" to passwordErrorMessage, // No spaces
            "" to passwordErrorMessage,
        )

        // Validate for username, firstName, and lastName separately
        usernameInvalidValues.forEach { (invalidValue, errorMessage) ->
            val invalidUser = UserCreateDTO(invalidValue, "TestUser@example.com", "Test", "User", "ValidPassword123!")
            val result = mvc.createUser(objectMapper, invalidUser)
            val responseContent = result.response.contentAsString
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()
            responseContent shouldContain errorMessage
        }

        emailInvalidValues.forEach { (invalidValue, errorMessage) ->
            val invalidUser = UserCreateDTO("TestUser", invalidValue, "Test", "User", "ValidPassword123!")
            val result = mvc.createUser(objectMapper, invalidUser)
            val responseContent = result.response.contentAsString
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()
            responseContent shouldContain errorMessage
        }

        firstNameInvalidValues.forEach { (invalidValue, errorMessage) ->
            val invalidUser = UserCreateDTO("TestUser", "TestUser@example.com", invalidValue, "User", "ValidPassword123!")
            val result = mvc.createUser(objectMapper, invalidUser)
            val responseContent = result.response.contentAsString
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()
            responseContent shouldContain errorMessage
        }

        lastNameInvalidValues.forEach { (invalidValue, errorMessage) ->
            val invalidUser = UserCreateDTO("TestUser", "TestUser@example.com", "Test", invalidValue, "ValidPassword123!")
            val result = mvc.createUser(objectMapper, invalidUser)
            val responseContent = result.response.contentAsString
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()
            responseContent shouldContain errorMessage
        }

        passwordInvalidValues.forEach { (invalidValue, errorMessage) ->
            val invalidUser = UserCreateDTO("TestUser", "TestUser@example.com", "Test", "User", invalidValue)
            val result = mvc.createUser(objectMapper, invalidUser)
            val responseContent = result.response.contentAsString
            result.response.status shouldBe HttpStatus.BAD_REQUEST.value()
            responseContent shouldContain errorMessage
        }
    }
})