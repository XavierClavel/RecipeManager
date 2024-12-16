package common.infodto

import common.enums.UserRole
import common.overviewdto.UserOverview
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Long,
    val username: String,
    val role: UserRole = UserRole.USER,
    val joinDate: Long,
    val bio: String,
    val recipesCount: Int,
    val likesCount: Int,
    val cookbooksCount: Int,
) {
    fun toOverview() = UserOverview(
        id = this.id,
        username = this.username,
    )
}