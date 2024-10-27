package common.dto

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val role: UserRole = UserRole.USER,
)