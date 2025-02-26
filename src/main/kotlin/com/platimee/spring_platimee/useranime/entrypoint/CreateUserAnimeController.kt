package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.model.UserAnimeCreateDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.service.CreateUserAnimeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-anime")
class CreateUserAnimeController(private val createUserAnimeService: CreateUserAnimeService) {

    @PostMapping
    fun createUserAnime(@Valid @RequestBody dto: UserAnimeCreateDTO): ResponseEntity<UserAnimeResponseDTO> {
        val createdRecord = createUserAnimeService.addUserAnime(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord)
    }
}
