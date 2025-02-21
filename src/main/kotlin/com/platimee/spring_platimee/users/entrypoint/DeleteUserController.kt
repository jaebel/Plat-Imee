package com.platimee.spring_platimee.users.entrypoint

import com.platimee.spring_platimee.users.service.DeleteUserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteUserController(private val deleteUserService: DeleteUserService) {

    @DeleteMapping("/api/v1/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserById(@PathVariable userId: Long) {
        deleteUserService.deleteUser(userId)
    }

}

//TODO globalise the paths