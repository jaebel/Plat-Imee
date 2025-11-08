package com.platimee.spring_platimee.entrypointTests.users

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult

internal fun MockMvc.createUser(objectMapper: ObjectMapper, user: UserCreateDTO) : MvcResult = this.post("/api/v1/users"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.deleteUser(userId: Long) : MvcResult = this.delete("/api/v1/users/$userId").andReturn()

internal fun MockMvc.deleteCurrentUser(username: String): MvcResult {
    val requestBuilder = org.springframework.test.web.servlet.request.MockMvcRequestBuilders
        .delete("/api/v1/users/me")
        .with(
            org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                .user(username)
        )

    return this.perform(requestBuilder).andReturn()
}

internal fun MockMvc.updateUser(objectMapper: ObjectMapper, user: UserUpdateDTO, userId: Long) : MvcResult = this.patch("/api/v1/users/$userId"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.getUser(userId: Long) : MvcResult = this.get("/api/v1/users/$userId").andReturn()

internal fun MockMvc.getAllUsers() : MvcResult = this.get("/api/v1/users").andReturn()

// In these tests, we don’t actually log in or generate a JWT.
// Instead, we simulate an already-authenticated by having this fulfil the
// 'authentication: Authentication' argument from which the endpoint only really needs it to have a name

// it works because your controller uses Spring Security’s built-in mechanism. If it used the manual JWT parsing
// as with the get endpoint then it would fail with the custom token validation method in JwtUtil,
// and with simply manually decoding/ deconstructing the token

internal fun MockMvc.updateUser(
    objectMapper: ObjectMapper,
    user: UserUpdateDTO,
    userId: Long,
    username: String,
    vararg roles: String
): MvcResult {
    val requestBuilder =
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .patch("/api/v1/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user))
            .with(
                org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                    .user(username)
                    .roles(*roles) // This isn't really relevant because plat-imee doesn't use roles at this time
            )

    return this.perform(requestBuilder).andReturn()
}

