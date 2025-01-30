package com.xavierclavel.services

import common.dto.UserDTO
import common.utils.URL.BASE_URL
import common.utils.URL.USER_URL
import org.koin.core.component.KoinComponent
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties
import io.github.cdimascio.dotenv.dotenv

class MailService: KoinComponent {
    companion object {
        const val MAIL_TITLE_VERIFICATION = "Cook&co account verification"
    }
    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    fun sendVerificationEmail(mail: String, token: String) {
        val link = "$BASE_URL/$USER_URL/verify?token=$token"
        val body = """
            Hello ! 
            
            Thank you for creating a Cook&Co account, I hope you will like using the application ! :)
            Please use this link to validate your account: $link
            
            Have a great day, 
            Xavier
        """.trimIndent()
        sendEmail(mail, MAIL_TITLE_VERIFICATION, body)
    }


    fun sendEmail(recipient: String, subject: String, body: String) {
        val dotenv = dotenv()
        val smtpEmail = dotenv["SMTP_EMAIL"]
        val smtpPassword = dotenv["SMTP_PASSWORD"]
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