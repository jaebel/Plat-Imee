package com.platimee.spring_platimee.auth.account.passwordreset

import com.platimee.spring_platimee.auth.account.email.MailService
import com.platimee.spring_platimee.auth.account.passwordreset.PasswordResetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/password")
class PasswordResetController(
    private val passwordResetService: PasswordResetService,
    private val mailService: MailService
) {

    @PostMapping("/forgot")
    fun requestPasswordReset(@RequestParam email: String): ResponseEntity<Map<String, String>> {
        val token = passwordResetService.createPasswordResetToken(email)
        mailService.sendPasswordResetEmail(email, token.token)
        return ResponseEntity.ok(mapOf("message" to "Password reset email sent successfully."))
    }

    @PostMapping("/reset")
    fun resetPassword(
        @RequestParam token: String,
        @RequestParam newPassword: String
    ): ResponseEntity<Map<String, String>> {
        val result = passwordResetService.resetPassword(token, newPassword)
        return ResponseEntity.ok(mapOf("message" to result))
    }
}
