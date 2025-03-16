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

internal fun MockMvc.updateUser(objectMapper: ObjectMapper, user: UserUpdateDTO, userId: Long) : MvcResult = this.patch("/api/v1/users/$userId"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.getUser(userId: Long) : MvcResult = this.get("/api/v1/users/$userId").andReturn()

internal fun MockMvc.getAllUsers() : MvcResult = this.get("/api/v1/users").andReturn()

internal fun MockMvc.updateUser(
    objectMapper: ObjectMapper,
    user: UserUpdateDTO,
    userId: Long,
    username: String? = null,
    vararg roles: String
): MvcResult {
    val requestBuilder =
        // Explicitly call the Java MockMvcRequestBuilders.patch(...) method
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .patch("/api/v1/users/$userId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user))

    // Optionally attach a mock user
    if (!username.isNullOrEmpty()) {
        requestBuilder.with(
            org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                .user(username)
                .roles(*roles)
        )
    }

    // Perform the request
    return this.perform(requestBuilder).andReturn()
}
