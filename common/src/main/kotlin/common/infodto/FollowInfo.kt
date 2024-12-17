package common.infodto

import common.enums.UserRole
import common.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class FollowInfo(
    val user: UserOverview,
    val followedSince: Long,
)