package com.xavierclavel.services

import com.xavierclavel.utils.Configuration
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.utils.URL.BASE_URL
import common.utils.URL.FRONTEND_URL
import common.utils.URL.USER_URL
import org.koin.core.component.KoinComponent
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties
import io.github.cdimascio.dotenv.dotenv
import org.koin.core.component.inject

class MailService: KoinComponent {
    companion object {
        const val MAIL_TITLE_VERIFICATION = "Cook&co account verification"
    }
    val configuration: Configuration by inject()

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    fun sendVerificationEmail(mail: String, token: String) {
        val link = "$FRONTEND_URL/user/verify?token=$token"
        val body = """
            Hello ! 
            
            Thank you for creating a Cook&Co account, I hope you will like using the application ! :)
            Please use this link to validate your account: $link
            
            Have a great day, 
            Xavier
        """.trimIndent()
        try {
            sendEmail(mail, MAIL_TITLE_VERIFICATION, body)
        } catch (e: Exception) {
            logger.error{"Token: $token"}
        }

    }

    fun sendPasswordResetEmail(mail: String, token: String) {
        val link = "$FRONTEND_URL/user/reset-password?token=$token"
        val body = """
            Hello ! 
            
            A password reset was requested for the Cook&Co account linked to this email.
            Please use this link to reset your password: $link
            If you did not request a password reset, please ignore this mail.
            
            Have a great day, 
            Xavier
        """.trimIndent()
        try {
            sendEmail(mail, MAIL_TITLE_VERIFICATION, body)
        } catch (e: Exception) {
            logger.error{"Token: $token"}
        }

    }

    fun sendEmail(recipient: String, subject: String, body: String) {
        val smtpEmail = configuration.smtp.email
        val smtpPassword = configuration.smtp.password
        logger.info {configuration.smtp}
        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(smtpEmail, smtpPassword)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(smtpEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                setSubject(subject)
                setText(body)
            }
            Transport.send(message)
            println("Email sent successfully!")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}