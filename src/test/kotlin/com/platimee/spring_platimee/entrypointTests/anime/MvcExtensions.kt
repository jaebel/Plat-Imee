package com.platimee.spring_platimee.entrypointTests.anime

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

internal fun MockMvc.createAnime(objectMapper: ObjectMapper, anime: AnimeCreateDTO) : MvcResult = this.post("/api/v1/anime"){
    content = objectMapper.writeValueAsString(anime)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.updateAnime(objectMapper: ObjectMapper, anime: AnimeUpdateDTO, animeId: Long) : MvcResult = this.patch("/api/v1/anime/$animeId"){
    content = objectMapper.writeValueAsString(anime)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()