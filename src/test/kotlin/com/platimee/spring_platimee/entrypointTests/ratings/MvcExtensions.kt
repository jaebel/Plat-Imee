package com.platimee.spring_platimee.entrypointTests.ratings

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

internal fun MockMvc.createRating(objectMapper: ObjectMapper, user: RatingCreateDTO) : MvcResult = this.post("/api/v1/ratings"){
    content = objectMapper.writeValueAsString(user)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

