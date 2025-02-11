package com.platimee.spring_platimee.entrypoint

import com.platimee.spring_platimee.model.UserCreateDTO
import com.platimee.spring_platimee.model.UserResponseDTO
import com.platimee.spring_platimee.service.CreateUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateUserController(private val createUserService: CreateUserService) {

    @PostMapping(
        path = ["/api/v1/users"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUserAccount(@Valid @RequestBody userCreateDto: UserCreateDTO): ResponseEntity<UserResponseDTO> {
        val savedUserDto = createUserService.addUser(userCreateDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto)
    }
}