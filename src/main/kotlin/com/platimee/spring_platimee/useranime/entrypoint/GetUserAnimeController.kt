package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.service.GetUserAnimeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-anime")
class GetUserAnimeController(private val getUserAnimeService: GetUserAnimeService) {

    // Get a record by the id of the actual record
    @GetMapping("/{id}")
    fun getUserAnime(@PathVariable id: Long): ResponseEntity<UserAnimeResponseDTO> {
        val record = getUserAnimeService.getUserAnime(id)
        return ResponseEntity.ok(record)
    }

    // Get all records under a userId
    @GetMapping(params = ["userId"])
    fun getUserAnimeByUser(@RequestParam userId: Long): ResponseEntity<List<UserAnimeResponseDTO>> {
        val records = getUserAnimeService.getUserAnimeByUserId(userId)
        return ResponseEntity.ok(records)
    }

}
