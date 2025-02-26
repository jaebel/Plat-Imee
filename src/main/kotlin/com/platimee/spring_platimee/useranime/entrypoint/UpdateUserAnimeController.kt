package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.model.UserAnimeUpdateDTO
import com.platimee.spring_platimee.useranime.service.UpdateUserAnimeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-anime")
class UpdateUserAnimeController(private val updateUserAnimeService: UpdateUserAnimeService) {

    @PatchMapping("/{id}")
    fun updateUserAnime(
        @PathVariable id: Long,
        @Valid @RequestBody dto: UserAnimeUpdateDTO
    ): ResponseEntity<UserAnimeResponseDTO> {
        val updatedRecord = updateUserAnimeService.updateUserAnime(id, dto)
        return ResponseEntity.ok(updatedRecord)
    }
}
