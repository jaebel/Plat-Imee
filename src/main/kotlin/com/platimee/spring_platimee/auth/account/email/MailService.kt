package com.platimee.spring_platimee.auth.account.email

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender
) {

    // Send account verification email
    fun sendVerificationEmail(recipientEmail: String, token: String) {
        val verificationLink = "http://localhost:8080/api/v1/users/verify?token=$token"
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
//        val resetLink = "http://localhost:8080/api/v1/password/reset?token=$token"
        val resetLink = "http://localhost:3000/reset-password?token=$token"

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
                    This link will expire in 1 hour. If you didn’t request this, you can safely ignore this email.
                </p>
            </body>
            </html>
        """.trimIndent()

        sendEmail(recipientEmail, subject, htmlContent)
    }

    private fun sendEmail(to: String, subject: String, htmlContent: String) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)
        mailSender.send(mimeMessage)
    }
}
