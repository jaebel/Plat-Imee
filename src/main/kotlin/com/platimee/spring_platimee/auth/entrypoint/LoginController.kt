package com.platimee.spring_platimee.auth.entrypoint

import com.platimee.spring_platimee.auth.model.LoginRequestDTO
import com.platimee.spring_platimee.auth.model.LoginResponseDTO
import com.platimee.spring_platimee.auth.service.LoginService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class LoginController(private val loginService: LoginService) {

    @PostMapping(
        path = ["/login"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(@Valid @RequestBody loginRequest: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val response = loginService.login(loginRequest)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}
