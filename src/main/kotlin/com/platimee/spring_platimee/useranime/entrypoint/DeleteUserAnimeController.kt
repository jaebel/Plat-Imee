package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.service.DeleteUserAnimeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user-anime")
class DeleteUserAnimeController(private val deleteUserAnimeService: DeleteUserAnimeService) {

    @DeleteMapping("/{id}")
    fun deleteUserAnime(@PathVariable id: Long): ResponseEntity<Void> {
        deleteUserAnimeService.deleteUserAnime(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
