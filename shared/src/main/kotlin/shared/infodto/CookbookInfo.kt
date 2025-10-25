package shared.infodto

import shared.enums.Visibility
import shared.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class CookbookInfo(
    val id: Long,
    val version: Long,
    val title: String,
    val visibility: Visibility,
    val description: String = "",
    val recipesCount: Int,
    val usersCount: Int,
    val members: List<UserOverview>,
)