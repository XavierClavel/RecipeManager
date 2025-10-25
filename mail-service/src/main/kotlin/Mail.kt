import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.Properties

class Mail(
    val recipient: String,
    val subject: String,
    val body: String
) {
    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    fun send() {
        val smtpEmail = System.getenv("COOKNCO_SMTP_ADDRESS")
        val smtpPassword = System.getenv("COOKNCO_SMTP_PASSWORD")
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