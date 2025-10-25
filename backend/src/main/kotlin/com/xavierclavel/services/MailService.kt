package com.xavierclavel.services

import com.xavierclavel.utils.Configuration
import com.xavierclavel.utils.logger
import shared.enums.Locale
import org.koin.core.component.KoinComponent
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties
import org.koin.core.component.inject

class MailService: KoinComponent {
    companion object {
        val ACCOUNT_VERIFICATION_TITLE = mapOf(
            Locale.EN to "Cook&Co account verification",
            Locale.FR to "Vérification de ton compte Cook&Co",
        )
        val ACCOUNT_VERIFICATION_CONTENT = mapOf(
            Locale.EN to """
                    Hello ! 
                    
                    Thank you for creating a Cook&Co account, I hope you will like using the application! :)
                    Please use this link to validate your account: %s
                    
                    Have a great day, 
                    Cook&Co team
                """.trimIndent(),
            Locale.FR to """
                    Hello !
                    
                    Merci d'avoir créé un compte Cook&Co, j'espère que tu apprécieras l'application ! :)
                    Utilises le lien suivant pour valider ton compte: %s
                    
                    Passes une bonne journée !
                    L'équipe Cook&Co
                """.trimIndent(),
        )

        val PASSWORD_RESET_TITLE = mapOf(
            Locale.EN to "Cook&Co password reset",
            Locale.FR to "Réinitialisation de ton mot de passe Cook&Co",
        )
        val PASSWORD_RESET_CONTENT = mapOf(
            Locale.EN to """
                    Hello! 
                    
                    A password reset was requested for the Cook&Co account linked to this email.
                    Please use this link to reset your password: %s
                    If you did not request a password reset, please ignore this mail.
                    
                    Have a great day, 
                    Cook&Co team
                """.trimIndent(),
            Locale.FR to """
                    Bonjour !
                    
                    Une demande de réinitialisation de mot de passe a été soumise pour le compte Cook&Co lié à cette addresse mail.
                    Tu peux utiliser le lien suivant pour réinitialiser ton mot de passe: %s
                    Si tu n'es pas l'auteur de cette demande, tu peux ignorer ce mail.
                    
                    Passes une bonne journée !
                    L'équipe Cook&Co
                """.trimIndent(),
        )
    }
    val configuration: Configuration by inject()

    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    fun sendVerificationEmail(mail: String, token: String, locale: Locale) {
        val link = "${configuration.frontend.url}/user/verify?token=$token"
        try {
            sendEmail(mail, ACCOUNT_VERIFICATION_TITLE[locale]!!, ACCOUNT_VERIFICATION_CONTENT[locale]!!.format(link))
        } catch (e: Exception) {
            logger.error{"Token: $token"}
        }

    }

    fun sendPasswordResetEmail(mail: String, token: String, locale: Locale) {
        val link = "${configuration.frontend.url}/password/reset/new?token=$token"
        try {
            sendEmail(mail, PASSWORD_RESET_TITLE[locale]!!, PASSWORD_RESET_CONTENT[locale]!!.format(link))
        } catch (e: Exception) {
            logger.error {e.message}
            logger.error{"Token: $token"}
        }

    }

    fun sendEmail(recipient: String, subject: String, body: String) {
        val smtpEmail = configuration.smtp.email
        val smtpPassword = configuration.smtp.password
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
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}