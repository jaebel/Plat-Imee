package com.platimee.spring_platimee.auth.account.verification

import com.platimee.spring_platimee.auth.account.email.MailService
import com.platimee.spring_platimee.auth.account.verification.VerificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
    fun confirmVerification(@RequestParam token: String): ResponseEntity<String> {
        val result = verificationService.verifyAccount(token)
        return ResponseEntity.ok(result)
    }
}