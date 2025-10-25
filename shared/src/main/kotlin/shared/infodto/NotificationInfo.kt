package shared.infodto

import shared.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class NotificationInfo(
    val followersPending : List<UserOverview>
)