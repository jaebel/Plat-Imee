package com.platimee.spring_platimee.entrypointTests.recommendation

import com.fasterxml.jackson.databind.ObjectMapper
import com.platimee.spring_platimee.entrypointTests.IntegrationTestSpec
import com.platimee.spring_platimee.recommendation.model.RecResponseDTO
import com.platimee.spring_platimee.recommendation.service.GetRecommendationService
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class GetRecommendationTests(
    val mvc: MockMvc,
    val objectMapper: ObjectMapper,
    val getRecommendationService: GetRecommendationService,
    val restTemplate: RestTemplate
) : IntegrationTestSpec({

    beforeEach {
        clearAllMocks()
    }

    test("GET /api/v1/recs/{userId} returns dummy recommendations") {
        val dummyRecommendations = arrayOf(
            RecResponseDTO(malId = 101L),
            RecResponseDTO(malId = 102L)
        )
        val jsonResponse = objectMapper.writeValueAsString(dummyRecommendations)
        val expectedUrl = "http://localhost:5000/api/recommendations"

        // Set up the MockRestServiceServer to simulate the external recommendation service
        val mockServer = MockRestServiceServer.createServer(restTemplate)
        mockServer.expect(requestTo(expectedUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON))

        val result = mvc.getRecommendations(123L)
        result.response.status shouldBe 200

        // Deserialize and validate the response
        val recs = objectMapper.readValue(result.response.contentAsString, Array<RecResponseDTO>::class.java)
        recs.size shouldBe 2
        recs[0].malId shouldBe 101L
        recs[1].malId shouldBe 102L

        // Verify that the external call was made as expected
        mockServer.verify()
    }

    test("Can retrieve dummy recommendations from recommendation service") {
        val dummyRecommendations = arrayOf(
            RecResponseDTO(malId = 101L),
            RecResponseDTO(malId = 102L)
        )
        val jsonResponse = objectMapper.writeValueAsString(dummyRecommendations)
        // I may change expected url to something else hence the name
        val expectedUrl = "http://localhost:5000/api/recommendations"

        // Set up the MockRestServiceServer to simulate the external service
        val mockServer = MockRestServiceServer.createServer(restTemplate)
        mockServer.expect(requestTo(expectedUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON))

        // Call the service - note that GetRecommendationService will internally build the payload
        val recommendations = getRecommendationService.getRecommendations(123L)

        recommendations.size shouldBe 2
        recommendations[0].malId shouldBe 101L
        recommendations[1].malId shouldBe 102L

        mockServer.verify()
    }
})
