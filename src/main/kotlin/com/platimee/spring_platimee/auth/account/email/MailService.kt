package com.platimee.spring_platimee.auth.account.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MailService(
    private val webClient: WebClient,
    @Value("\${FRONTEND_URL}")
    private val frontendUrl: String,
    @Value("\${resend.api-key}")
    private val apiKey: String,
    @Value("\${resend.from}")
    private val fromEmail: String
) {

    // Send account verification email
    fun sendVerificationEmail(recipientEmail: String, token: String) {
        val verificationLink = "$frontendUrl/verify?token=$token"
        val subject = "Platimee — Verify Your Account"
        val htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Welcome to Platimee!</h2>
                <p>
                    Please verify your email by clicking or copying the link below:
                    <br><br>
                    <a href="$verificationLink" target="_blank">$verificationLink</a>
                </p>
                <p style="color:gray;font-size:12px;">
                    This link will expire in 24 hours.
                </p>
            </body>
            </html>
        """.trimIndent()

        sendEmail(recipientEmail, subject, htmlContent)
    }

    // Send password reset email
    fun sendPasswordResetEmail(recipientEmail: String, token: String) {
        val resetLink = "$frontendUrl/reset-password?token=$token"
        val subject = "Platimee — Reset Your Password"
        val htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Password Reset Request</h2>
                <p>
                    We received a request to reset your password.
                    <br><br>
                    You can reset it by clicking or copying the link below:
                    <br><br>
                    <a href="$resetLink" target="_blank">$resetLink</a>
                </p>
                <p style="color:gray;font-size:12px;">
                    This link will expire in 1 hour. If you didn't request this, you can safely ignore this email.
                </p>
            </body>
            </html>
        """.trimIndent()

        sendEmail(recipientEmail, subject, htmlContent)
    }

    private fun sendEmail(to: String, subject: String, html: String) {
        val payload = mapOf(
            "from" to fromEmail,
            "to" to listOf(to),  // Resend API expects an array
            "subject" to subject,
            "html" to html
        )

        try {
            val response = webClient.post()
                .uri("https://api.resend.com/emails")
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .onStatus({ it.isError }) { clientResponse ->
                    clientResponse.bodyToMono(String::class.java)
                        .map { body -> RuntimeException("Resend API error: ${clientResponse.statusCode()} - $body") }
                }
                .bodyToMono(String::class.java)
                .block()

            println("📧 Email sent successfully via Resend to $to: $response")
        } catch (e: Exception) {
            println("Failed to send email to $to: ${e.message}")
            throw RuntimeException("Failed to send email to $to", e)
        }
    }
}