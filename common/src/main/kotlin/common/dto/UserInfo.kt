package common.dto

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val username: String,
    val role: UserRole = UserRole.USER,
)