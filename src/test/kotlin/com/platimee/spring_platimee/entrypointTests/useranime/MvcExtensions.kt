package com.platimee.spring_platimee.entrypointTests.useranime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.useranime.model.UserAnimeCreateDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

internal fun MockMvc.createUserAnime(objectMapper: ObjectMapper, dto: UserAnimeCreateDTO): MvcResult =
    this.post("/api/v1/user-anime") {
        content = objectMapper.writeValueAsString(dto)
        contentType = MediaType.APPLICATION_JSON
    }.andReturn()

internal fun MockMvc.updateUserAnime(objectMapper: ObjectMapper, dto: UserAnimeUpdateDTO, id: Long): MvcResult =
    this.patch("/api/v1/user-anime/$id") {
        content = objectMapper.writeValueAsString(dto)
        contentType = MediaType.APPLICATION_JSON
    }.andReturn()

internal fun MockMvc.deleteUserAnime(id: Long): MvcResult =
    this.delete("/api/v1/user-anime/$id").andReturn()

internal fun MockMvc.getUserAnime(id: Long): MvcResult =
    this.get("/api/v1/user-anime/$id").andReturn()

