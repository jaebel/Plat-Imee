package com.platimee.spring_platimee.users.entrypoint

import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.service.GetUserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetUserController(
    private val getUserService: GetUserService
) {

    @GetMapping(
        path = ["/api/v1/users"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllUsers(): ResponseEntity<List<UserResponseDTO>> {
        val users = getUserService.getAll()
        return ResponseEntity.status(HttpStatus.OK).body(users)
    }

    // GET a single user by ID
    @GetMapping(
        path = ["/api/v1/users/{userId:[0-9]+}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserResponseDTO> {
        val user = getUserService.getById(userId)
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }

    // NOTE: that this endpoint demonstrates manual JWT validation using JwtUtil
    // It could handle token validation with spring security instead (it would be cleaner)
    @GetMapping("/api/v1/users/me")
    fun getCurrentUser(request: HttpServletRequest): ResponseEntity<UserResponseDTO> {
        val authHeader = request.getHeader("Authorization")
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        // The header should be in the format "Bearer <token>"
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val token = authHeader.substring(7)
        val userResponse = getUserService.getCurrentUserByToken(token)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        return ResponseEntity.ok(userResponse)
    }
}
