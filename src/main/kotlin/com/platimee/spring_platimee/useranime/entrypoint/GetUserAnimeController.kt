package com.platimee.spring_platimee.useranime.entrypoint

import com.platimee.spring_platimee.useranime.model.UserAnimeResponseDTO
import com.platimee.spring_platimee.useranime.service.GetUserAnimeService
import com.platimee.spring_platimee.users.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/api/v1/user-anime")
class GetUserAnimeController(
    private val getUserAnimeService: GetUserAnimeService,
    private val userRepository: UserRepository,
    @Value("\${JWT_SECRET}")
    private val secretKey: String
) {

    // Get a record by the id of the actual record
    @GetMapping("/{id}")
    fun getUserAnime(@PathVariable id: Long): ResponseEntity<UserAnimeResponseDTO> {
        val record = getUserAnimeService.getUserAnime(id)
        return ResponseEntity.ok(record)
    }

    // Get all records under a userId (legacy and insecure)
    @GetMapping(params = ["userId"])
    fun getUserAnimeByUser(@RequestParam userId: Long): ResponseEntity<List<UserAnimeResponseDTO>> {
        val records = getUserAnimeService.getUserAnimeByUserId(userId)
        return ResponseEntity.ok(records)
    }

    // Secure endpoint using JWT token to get only the current user's list
    @GetMapping("/me")
    fun getCurrentUserAnime(request: HttpServletRequest): ResponseEntity<List<UserAnimeResponseDTO>> {
        val authHeader = request.getHeader("Authorization")
            ?: return ResponseEntity.status(401).build()

        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build()
        }

        val token = authHeader.substring(7)

        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject

        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.status(404).build()

        val records = getUserAnimeService.getUserAnimeByUserId(user.userId!!)
        return ResponseEntity.ok(records)
    }
}
