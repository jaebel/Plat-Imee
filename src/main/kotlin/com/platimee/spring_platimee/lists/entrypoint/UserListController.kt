package com.platimee.spring_platimee.lists.entrypoint

import com.platimee.spring_platimee.lists.model.UserListCreateDTO
import com.platimee.spring_platimee.lists.model.UserListResponseDTO
import com.platimee.spring_platimee.lists.service.UserListService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user-lists")
class UserListController(private val userListService: UserListService) {

    @PostMapping
    fun createUserList(@Valid @RequestBody dto: UserListCreateDTO): ResponseEntity<UserListResponseDTO> {
        val createdList = userListService.createUserList(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList)
    }

    @GetMapping("/{listId}")
    fun getUserList(@PathVariable listId: Long): ResponseEntity<UserListResponseDTO> {
        val userList = userListService.getUserList(listId)
        return ResponseEntity.ok(userList)
    }

    @PostMapping("/{listId}/anime/{animeId}")
    fun addAnimeToList(
        @PathVariable listId: Long,
        @PathVariable animeId: Long
    ): ResponseEntity<UserListResponseDTO> {
        val updatedList = userListService.addAnimeToList(listId, animeId)
        return ResponseEntity.ok(updatedList)
    }

}
