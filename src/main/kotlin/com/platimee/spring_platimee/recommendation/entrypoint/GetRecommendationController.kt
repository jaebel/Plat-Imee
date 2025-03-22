package com.platimee.spring_platimee.recommendation.entrypoint

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.platimee.spring_platimee.recommendation.model.RecResponseDTO
import com.platimee.spring_platimee.recommendation.service.GetRecommendationService

@RestController
class GetRecommendationController(private val getRecommendationService: GetRecommendationService) {

    // GET recommendations for a given user ID (explicit)
    @GetMapping("/api/v1/recs/{userId:[0-9]+}")
    fun getRecommendationsByUserId(@PathVariable userId: Long): ResponseEntity<List<RecResponseDTO>> {
        val recommendations = getRecommendationService.getRecommendations(userId)
        return ResponseEntity.status(HttpStatus.OK).body(recommendations)
    }
}

