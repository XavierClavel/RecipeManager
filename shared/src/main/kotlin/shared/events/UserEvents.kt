package shared.events

import kotlinx.serialization.Serializable


@Serializable
data class FollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : DomainEvent() {
    override val type = "followed_user"
}

@Serializable
data class UnfollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : DomainEvent() {
    override val type = "unfollowed_user"
}

@Serializable
data class NotificationsToggledEvent(
    val userId: Long,
    val enabled: Boolean
) : DomainEvent() {
    override val type = "notifications_toggled"
}
