package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FollowEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-follows"
}


@Serializable
@SerialName("followed_user")
data class FollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : FollowEvent() {
    override val type = "followed_user"
    override fun getKey() = followedId.toString()
}

@Serializable
@SerialName("unfollowed_user")
data class UnfollowedUserEvent(
    val followerId: Long,
    val followedId: Long
) : FollowEvent() {
    override val type = "unfollowed_user"
    override fun getKey() = followedId.toString()
}
