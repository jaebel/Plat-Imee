package com.platimee.spring_platimee.anime.entrypoint

import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.model.AnimeUpdateDTO
import com.platimee.spring_platimee.anime.service.UpdateAnimeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/anime")
class UpdateAnimeController(
    private val updateAnimeService: UpdateAnimeService
) {

    @PatchMapping(
        path = ["/{animeId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateAnime(@PathVariable animeId: Long, @Valid @RequestBody animeUpdateRequestO: AnimeUpdateDTO): ResponseEntity<AnimeResponseDTO> {
        val updatedAnime = updateAnimeService.updateAnime(animeId, animeUpdateRequestO)
        return ResponseEntity.status(HttpStatus.OK).body(updatedAnime)
    }
}
