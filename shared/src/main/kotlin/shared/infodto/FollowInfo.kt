package shared.infodto

import shared.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class FollowInfo(
    val user: UserOverview,
    val followedSince: Long,
    val pending: Boolean,
)