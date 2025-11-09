package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.auth.account.email.MailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class VerificationController(
    private val verificationService: VerificationService,
    private val mailService: MailService
) {

    @PostMapping("/{userId}/verification")
    fun sendVerification(@PathVariable userId: Long): ResponseEntity<Map<String, String>> {
        val token = verificationService.createVerificationToken(userId)
        mailService.sendVerificationEmail(token.user!!.email, token.token)
        return ResponseEntity.ok(mapOf("message" to "Verification email sent successfully."))
    }

    @GetMapping("/verify")
    fun confirmVerification(@RequestParam token: String): ResponseEntity<Map<String, String>> {
        val result = verificationService.verifyAccount(token)
        return ResponseEntity.ok(mapOf("message" to result))

    }
}