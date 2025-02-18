package com.platimee.spring_platimee.entrypointTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.model.UserCreateDTO
import com.platimee.spring_platimee.model.UserUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

internal fun MockMvc.createUser(objectMapper: ObjectMapper, user: UserCreateDTO) : MvcResult = this.post("/api/v1/users"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.deleteUser(userId: Long) : MvcResult = this.delete("/api/v1/users/$userId").andReturn()

internal fun MockMvc.updateUser(objectMapper: ObjectMapper, user: UserUpdateDTO, userId: Long) : MvcResult = this.patch("/api/v1/users/$userId"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()