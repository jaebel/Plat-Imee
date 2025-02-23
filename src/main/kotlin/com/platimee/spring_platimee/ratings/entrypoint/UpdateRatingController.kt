package com.platimee.spring_platimee.ratings.entrypoint

import com.platimee.spring_platimee.ratings.service.UpdateRatingService
import com.platimee.spring_platimee.ratings.model.RatingUpdateDTO
import com.platimee.spring_platimee.ratings.model.RatingResponseDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/ratings")
class UpdateRatingController(private val updateRatingService: UpdateRatingService) {

    @PutMapping(
        value = ["/{ratingId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateRating(
        @PathVariable ratingId: Long,
        @Valid @RequestBody dto: RatingUpdateDTO
    ): ResponseEntity<RatingResponseDTO> {
        val responseDto = updateRatingService.updateRating(ratingId, dto.rating)
        return ResponseEntity.status(HttpStatus.OK).body(responseDto)
    }
}
