package com.platimee.spring_platimee.entrypoint

import com.platimee.spring_platimee.model.User
import com.platimee.spring_platimee.service.GetUserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetUserController(private val getUserService: GetUserService) {

    // GET all users
    @GetMapping(
        path = ["/api/v1/users"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = getUserService.getAll()
        return ResponseEntity.status(HttpStatus.OK).body(users)
    }

    // GET a single user by ID
    @GetMapping(
        path = ["/api/v1/users/{userId}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUserById(@PathVariable userId: Long): ResponseEntity<User> {
        val user = getUserService.getById(userId)
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }
}
