package com.platimee.spring_platimee.entrypointTests.recommendation

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.get

internal fun MockMvc.getRecommendations(userId: Long): MvcResult =
    this.get("/api/v1/recs/$userId") {
        contentType = MediaType.APPLICATION_JSON
    }.andReturn()