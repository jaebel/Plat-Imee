package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.service.GetUserAnimeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-anime")
class GetUserAnimeController(private val getUserAnimeService: GetUserAnimeService) {

    @GetMapping("/{id}")
    fun getUserAnime(@PathVariable id: Long): ResponseEntity<UserAnimeResponseDTO> {
        val record = getUserAnimeService.getUserAnime(id)
        return ResponseEntity.ok(record)
    }
}
