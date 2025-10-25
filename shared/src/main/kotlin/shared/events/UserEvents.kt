package shared.events

import kotlinx.serialization.Serializable


@Serializable
data class FollowedUserEvent(
    val followerId: String,
    val followedId: String
) : DomainEvent() {
    override val type = "followed_user"
}

@Serializable
data class UnfollowedUserEvent(
    val followerId: String,
    val followedId: String
) : DomainEvent() {
    override val type = "unfollowed_user"
}

@Serializable
data class NotificationsToggledEvent(
    val userId: String,
    val enabled: Boolean
) : DomainEvent() {
    override val type = "notifications_toggled"
}
