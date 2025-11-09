package com.platimee.spring_platimee.users.entrypoint

import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.model.UserUpdateDTO
import com.platimee.spring_platimee.users.service.UpdateUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateUserController(private val updateUserService: UpdateUserService) {

    @PatchMapping(
        path = ["/api/v1/users/{userId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUserById(
        @PathVariable userId: Long,
        @Valid @RequestBody userUpdateRequest: UserUpdateDTO,
        authentication: Authentication
    ): ResponseEntity<UserResponseDTO> {
        val updatedUser = updateUserService.updateUser(userId, userUpdateRequest, authentication.name)
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser)
    }
}

// NOTE: Updating usernames is not currently possible because they are the subject of authentication tokens
// If I update a username then the users token would no longer be valid and i'd have to force a logout and re-login
//TODO globalise the paths