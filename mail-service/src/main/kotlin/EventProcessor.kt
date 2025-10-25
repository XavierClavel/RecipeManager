import io.ebean.DB
import shared.events.DomainEvent
import shared.events.FollowedUserEvent
import shared.events.NewRecipeEvent
import shared.events.NotificationsToggledEvent
import shared.events.UnfollowedUserEvent


class EventProcessor {
    fun handle(event: DomainEvent) {
        when (event) {
            is FollowedUserEvent -> handleFollow(event)
            is UnfollowedUserEvent -> handleUnfollow(event)
            is NotificationsToggledEvent -> handleNotifications(event)
            is NewRecipeEvent -> handleNewRecipe(event)
        }
    }

    private fun handleFollow(e: FollowedUserEvent) {
        val followerId = e.followerId
        val followedId = e.followedId
        Follower(followerId = followerId, followedId = followedId).save()
    }

    private fun handleUnfollow(e: UnfollowedUserEvent) {
        val followerId = e.followerId
        val followedId = e.followedId
        DB.find(Follower::class.java)
            .where().eq("follower_id", followerId).eq("followed_id", followedId)
            .findOne()?.delete()
    }

    private fun handleNotifications(e: NotificationsToggledEvent) {
        val user = DB.find(User::class.java, e.userId) ?: return
        user.notificationsEnabled = e.enabled
        user.save()
    }

    private fun handleNewRecipe(e: NewRecipeEvent) {
        val authorId = e.authorId
        val followers = DB.createQuery(Follower::class.java)
            .where().eq("followed_id", authorId).findList()

        for (f in followers) {
            val user = DB.find(User::class.java, f.followerId) ?: continue
            if (user.notificationsEnabled) {
                Mail(user.email, PASSWORD_RESET_TITLE[user.locale], PASSWORD_RESET)
                emailSender.sendNewRecipeEmail(user.email, e.authorName, e.title, e.imageUrl)
            }
        }
    }
}
