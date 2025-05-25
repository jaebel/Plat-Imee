package com.platimee.spring_platimee.users.entrypoint

import com.platimee.spring_platimee.users.model.UserDtoMapper
import com.platimee.spring_platimee.users.model.UserResponseDTO
import com.platimee.spring_platimee.users.repository.UserRepository
import com.platimee.spring_platimee.users.service.GetUserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets

@RestController
class GetUserController(
    private val getUserService: GetUserService,
    private val userRepository: UserRepository,
    @Value("\${JWT_SECRET}")
    private val secretKey: String
) {

    // GET all users
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

    @GetMapping("/api/v1/users/me")
    fun getCurrentUser(request: HttpServletRequest): ResponseEntity<UserResponseDTO> {
        // Retrieve the Authorization header from the request
        val authHeader = request.getHeader("Authorization")
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        // The header should be in the format "Bearer <token>"
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val token = authHeader.substring(7)

        // Parse the JWT token to extract the subject (username)
        // Use the same secret key used in token generation
        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject

        // Retrieve the user from the database using the username.
        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        // Map the user entity to a response DTO and return.
        val userResponse = UserDtoMapper.toResponseDto(user)
        return ResponseEntity.ok(userResponse)
    }
}
