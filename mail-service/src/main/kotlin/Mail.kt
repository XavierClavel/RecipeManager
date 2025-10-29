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
    val templatePath: String,
    val templateMap: Map<String, String> = HashMap(),
) {
    val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    companion object {
        val smtpEmail: String = System.getenv("COOKNCO_SMTP_ADDRESS")
        val smtpPassword: String = System.getenv("COOKNCO_SMTP_PASSWORD")
    }



    fun send() {
        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(smtpEmail, smtpPassword)
            }
        })

        val mailTemplate =  object {}.javaClass.getResource(templatePath)?.readText() ?: throw Exception("No template found for $templatePath")
        val lines = mailTemplate.lines()
        val mailSubject = lines.first().substringAfter("<subject>").substringBefore("</subject>")
        val mailBody = lines
            .subList(1, lines.size)
            .joinToString("\n")
        templateMap.forEach { (key, value) ->
            mailBody.replace("{{$key}}", value)
        }

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(smtpEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                setSubject(mailSubject)
                setText(mailBody, "utf-8", "html")
            }
            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}