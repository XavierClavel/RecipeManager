package common.infodto

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Long,
    val username: String,
    val role: UserRole = UserRole.USER,
    val joinDate: Long,
    val bio: String,
)