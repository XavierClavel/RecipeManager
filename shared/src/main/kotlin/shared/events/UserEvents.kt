package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : DomainEvent() {
    override val type = "followed_user"
}

@Serializable
@SerialName("unfollowed_user")
data class UnfollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : DomainEvent() {
    override val type = "unfollowed_user"
}

@Serializable
@SerialName("notifications_toggled")
data class NotificationsToggledEvent(
    val userId: Long,
    val enabled: Boolean
) : DomainEvent() {
    override val type = "notifications_toggled"
}
