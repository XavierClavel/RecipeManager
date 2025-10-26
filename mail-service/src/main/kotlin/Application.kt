import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.launch
import models.Follower
import models.query.QFollower
import shared.events.FollowedUserEvent
import shared.events.KafkaEventConsumer
import shared.events.NewRecipeEvent
import shared.events.NotificationsToggledEvent
import shared.events.TestEvent
import shared.events.UnfollowedUserEvent

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val kafkaConsumer = KafkaEventConsumer("mail-service", listOf()) { event ->
        when (event) {
            is FollowedUserEvent -> handleFollow(event)
            is UnfollowedUserEvent -> handleUnfollow(event)
            is NotificationsToggledEvent -> handleNotifications(event)
            is NewRecipeEvent -> handleNewRecipe(event)
            is TestEvent -> handleTest(event)
        }
    }

    launch {
        kafkaConsumer.start()
    }

    routing {
        get("/health") { call.respondText("OK") }
    }
}

private fun handleFollow(e: FollowedUserEvent) {
    val followerId = e.followerId
    val followedId = e.followedId
    Follower(followerId = followerId, followedId = followedId).save()
}

private fun handleUnfollow(e: UnfollowedUserEvent) {
    QFollower()
        .followerId.eq(e.followerId)
        .followedId.eq(e.followedId)
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
    val mail = Mail("xrclavel@gmail.com", "/emails/account_verification_en.html")
    mail.send()
}