package common.infodto

import common.enums.UserRole
import common.enums.Visibility
import common.infodto.LikeInfo.User
import common.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class CookbookInfo(
    val id: Long,
    val title: String,
    val visibility: Visibility,
    val description: String = "",
    val recipesCount: Int,
    val usersCount: Int,
    val members: List<UserOverview>,
)