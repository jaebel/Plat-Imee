package com.platimee.spring_platimee.anime.entrypoint

import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.service.GetAnimeService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetAnimeController(private val getAnimeService: GetAnimeService) {

    // GET all anime
    @GetMapping(
        path = ["/api/v1/anime"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllAnime(): ResponseEntity<List<AnimeResponseDTO>> {
        val anime = getAnimeService.getAll()
        return ResponseEntity.status(HttpStatus.OK).body(anime)
    }

    // GET a single anime by ID
    @GetMapping(
        path = ["/api/v1/anime/{malId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAnimeById(@PathVariable malId: Long): ResponseEntity<AnimeResponseDTO> {
        val anime = getAnimeService.getById(malId)
        return ResponseEntity.status(HttpStatus.OK).body(anime)
    }
}
