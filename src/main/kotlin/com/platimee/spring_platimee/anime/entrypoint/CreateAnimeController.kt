package com.platimee.spring_platimee.anime.entrypoint

import com.platimee.spring_platimee.anime.model.AnimeCreateDTO
import com.platimee.spring_platimee.anime.model.AnimeResponseDTO
import com.platimee.spring_platimee.anime.service.CreateAnimeService
import com.platimee.spring_platimee.users.model.UserCreateDTO
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.service.CreateUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateAnimeController(private val createAnimeService: CreateAnimeService) {

    @PostMapping(
        path = ["/api/v1/anime"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createAnime(@Valid @RequestBody animeCreateDto: AnimeCreateDTO): ResponseEntity<AnimeResponseDTO> {
        val savedAnimeDto = createAnimeService.addAnime(animeCreateDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimeDto)
    }
}