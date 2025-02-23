package com.platimee.spring_platimee.entrypointTests.ratings

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.ratings.model.RatingUpdateDTO
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

internal fun MockMvc.createRating(objectMapper: ObjectMapper, rating: RatingCreateDTO) : MvcResult = this.post("/api/v1/ratings"){
    content = objectMapper.writeValueAsString(rating)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

internal fun MockMvc.updateRating(objectMapper: ObjectMapper, rating: RatingUpdateDTO, ratingId: Long) : MvcResult = this.patch("/api/v1/ratings/$ratingId"){
    content = objectMapper.writeValueAsString(rating)
    contentType = MediaType.APPLICATION_JSON
}.andReturn()

