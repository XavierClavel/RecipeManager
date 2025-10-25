package shared.overviewdto

import shared.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserOverview(
    val id: Long,
    val role: UserRole,
    val version: Long,
    val username: String,
)