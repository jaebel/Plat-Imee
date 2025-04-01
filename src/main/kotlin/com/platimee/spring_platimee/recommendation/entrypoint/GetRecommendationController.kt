package com.platimee.spring_platimee.recommendation.entrypoint

import com.platimee.spring_platimee.recommendation.model.RecResponseDTO
import com.platimee.spring_platimee.recommendation.service.GetRecommendationService
import com.platimee.spring_platimee.users.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets

@RestController
class GetRecommendationController(
    private val getRecommendationService: GetRecommendationService,
    private val userRepository: UserRepository
) {

    // GET recommendations for a given user ID (explicit)
    @GetMapping("/api/v1/recs/{userId:[0-9]+}")
    fun getRecommendationsByUserId(
        @PathVariable userId: Long,
        @RequestParam(name = "safeSearch", defaultValue = "false") safeSearch: Boolean
    ): ResponseEntity<List<RecResponseDTO>> {
        val recommendations = getRecommendationService.getRecommendations(userId, safeSearch)
        return ResponseEntity.status(HttpStatus.OK).body(recommendations)
    }

    // GET recommendations for the current authenticated user
    @GetMapping("/api/v1/recs/me")
    fun getRecommendationsForCurrentUser(
        request: HttpServletRequest,
        @RequestParam(name = "safeSearch", defaultValue = "false") safeSearch: Boolean
    ): ResponseEntity<List<RecResponseDTO>> {
        val authHeader = request.getHeader("Authorization")
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val token = authHeader.substring(7)
        val secretKey = "ThisIsMy32CharMinimumLengthSecret!"
        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token)
            .body
        val username = claims.subject
        val user = userRepository.findByUsername(username)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        val recommendations = getRecommendationService.getRecommendations(user.userId!!, safeSearch)
        return ResponseEntity.ok(recommendations)
    }
}
