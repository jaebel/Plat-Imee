package com.platimee.spring_platimee.ratings.entrypoint

import com.platimee.spring_platimee.ratings.service.CreateRatingService
import com.platimee.spring_platimee.ratings.model.RatingCreateDTO
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/ratings")
class CreateRatingController(private val createRatingService: CreateRatingService) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addRating(@Valid @RequestBody dto: RatingCreateDTO): ResponseEntity<RatingResponseDTO> {
        val responseDto = createRatingService.addRating(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
    }
}
