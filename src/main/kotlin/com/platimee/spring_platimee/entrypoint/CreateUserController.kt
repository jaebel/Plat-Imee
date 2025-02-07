package com.platimee.spring_platimee.entrypoint

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.service.CreateUserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid

@RestController
class CreateUserController(private val createUserService: CreateUserService) {

    @PostMapping(
        path = ["/api/v1/users"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED) // remove this?
    fun createUserAccount(@Valid @RequestBody user: User): ResponseEntity<User> {
        val savedUser = createUserService.addUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }
}

// STILL WORK ON THIS FILE
// I left off looking into api models vs normal models for user @(you can see entityapi and entity in souvineerUpdated