import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.launch
import models.Follower
import models.User
import models.query.QFollower
import models.query.QUser
import shared.enums.Locale
import shared.events.AccountVerificationRequestedEvent
import shared.events.FollowedUserEvent
import shared.events.KafkaEventConsumer
import shared.events.NewRecipeEvent
import shared.events.NotificationsToggledEvent
import shared.events.PasswordResetRequestedEvent
import shared.events.TestEvent
import shared.events.UnfollowedUserEvent
import shared.events.UserCreatedEvent
import shared.utils.logger
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun main() {
    DatabaseManager.init()

    logger.info { "Server started." }
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
    logger.info { "Server closed."}
}

private val aesKey = SecretKeySpec(System.getenv("AES_KEY").toByteArray(), "AES")
private val frontendUrl = System.getenv("FRONTEND_URL")

fun Application.module() {

    val kafkaConsumer = KafkaEventConsumer("mail-service", listOf("cooknco-users", "cooknco-auth")) { event ->
        logger.info { "Received event: $event" }
        when (event) {
            is FollowedUserEvent -> handleFollow(event)
            is UnfollowedUserEvent -> handleUnfollow(event)
            is NotificationsToggledEvent -> handleNotifications(event)
            is TestEvent -> handleTest(event)
            is UserCreatedEvent -> handleUserCreation(event)
            is PasswordResetRequestedEvent -> handlePasswordResetRequest(event)
            is AccountVerificationRequestedEvent -> handleAccountVerificationRequested(event)
            else -> {}
        }
    }

    launch {
        kafkaConsumer.start()
    }

    routing {
        get("/health") { call.respondText("OK") }
    }
}

private fun handleUserCreation(event: UserCreatedEvent) {
    User(
        id = event.id,
        username = event.username,
        encryptedMail = event.mail,
    ).save()
}

private fun handleFollow(e: FollowedUserEvent) {
    val follower = QUser().id.eq(e.followerId).findOne()!!
    val followed = QUser().id.eq(e.followedId).findOne()!!
    Follower(follower = follower, followed = followed).save()
}

private fun handleUnfollow(e: UnfollowedUserEvent) {
    QFollower()
        .follower.id.eq(e.followerId)
        .followed.id.eq(e.followedId)
        .delete()
}

private fun handleNotifications(e: NotificationsToggledEvent) {
    /*
    val user = DB.find(User::class.java, e.userId) ?: return
    user.notificationsEnabled = e.enabled
    user.save()
     */
}

private fun handleNewRecipe(e: NewRecipeEvent) {
    /*
    val authorId = e.authorId
    val followers = DB.createQuery(Follower::class.java)
        .where().eq("followed_id", authorId).findList()

    for (f in followers) {
        val user = DB.find(User::class.java, f.followerId) ?: continue
        if (user.notificationsEnabled) {
            Mail(user.email, PASSWORD_RESET_TITLE[user.locale], PASSWORD_RESET).send()
        }
    }

     */
}

private fun handleTest(e: TestEvent) {
    logger.info {"Received test event"}
    val mail = Mail("xrclavel@gmail.com", "/emails/account_verification_en.html")
    mail.send()
}

private fun getMailAndLocale(id: Long): Pair<String, Locale> {
    val user = QUser().id.eq(id).findOne()!!
    return decrypt(user.encryptedMail) to user.locale
}


private fun getFollowersMail(id: Long): List<String> =
    QFollower().followed.id.eq(id).findList().map { decrypt(it.follower!!.encryptedMail) }


private fun handlePasswordResetRequest(e: PasswordResetRequestedEvent) {
    val (emailAddress, locale) = getMailAndLocale(e.userId)
    val template = when (locale) {
        Locale.EN -> "/emails/password_reset_en.html"
        Locale.FR -> "/emails/password_reset_fr.html"
    }
    val mail = Mail(emailAddress, template, mapOf(
        "link" to "$frontendUrl/password/reset/new?token=${e.token}",
        )
    )
    mail.send()
}

private fun handleAccountVerificationRequested(e: AccountVerificationRequestedEvent) {
    val (emailAddress, locale) = getMailAndLocale(e.userId)
    val template = when (locale) {
        Locale.EN -> "/emails/account_verification_en.html"
        Locale.FR -> "/emails/account_verification_fr.html"
    }
    val mail = Mail(emailAddress, template, mapOf(
        "link" to "$frontendUrl/user/verify?token=${e.token}",
        )
    )
    mail.send()
}

private fun decrypt(encryptedEmail: String): String {
    val decoded = Base64.getDecoder().decode(encryptedEmail)
    val ivBytes = decoded.copyOfRange(0, 16)
    val encryptedBytes = decoded.copyOfRange(16, decoded.size)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, aesKey, IvParameterSpec(ivBytes))
    return String(cipher.doFinal(encryptedBytes))
}