package com.platimee.spring_platimee.auth.account.verification.email

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import jakarta.mail.internet.MimeMessage

@Service
class MailService(
    private val mailSender: JavaMailSender
) {
    fun sendVerificationEmail(recipientEmail: String, token: String) {
        val verificationLink = "http://localhost:8080/api/v1/users/verify?token=$token"

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")

        val htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2>Welcome to Platimee!</h2>
                <p>
                    Please verify your email by clicking/ copying and pasting the link below:
                    <br><br>
                    <a href="$verificationLink" target="_blank">$verificationLink</a>
                </p>
                <br>
                <p style="color:gray;font-size:12px;">This link will expire in 24 hours.</p>
            </body>
            </html>
        """.trimIndent()

        helper.setTo(recipientEmail)
        helper.setSubject("Platimee — Verify Your Account")
        helper.setText(htmlContent, true)
        mailSender.send(mimeMessage)
    }
}
