package common.infodto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardReport(
    val usersCount: Int,
    val activeUsersCount: Int,
    val recipesCount: Int,
    val cookbooksCount: Int,
    val likesCount: Int,
)