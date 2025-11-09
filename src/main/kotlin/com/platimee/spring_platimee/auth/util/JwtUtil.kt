import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date

object JwtUtil {
    // Use environment variable for the secret key
    private val SECRET_KEY: String = System.getenv("JWT_SECRET")
    private const val EXPIRATION_TIME = 86400000L // 1 day

    fun generateToken(username: String): String {
        val now = Date()
        val expiryDate = Date(now.time + EXPIRATION_TIME)
        val keyBytes = SECRET_KEY.toByteArray(StandardCharsets.UTF_8)
        val key = Keys.hmacShaKeyFor(keyBytes)
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    // Spring security basically has token validation as a feature
    // if you use Authentication as an argument to controller functions and call .name to get the subject
    fun validateToken(token: String): String? {
        return try {
            val keyBytes = SECRET_KEY.toByteArray(StandardCharsets.UTF_8)
            val key = Keys.hmacShaKeyFor(keyBytes)
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
            claims.subject
        } catch (ex: Exception) {
            null
        }
    }
}
