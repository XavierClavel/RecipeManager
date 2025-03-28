package common.dto

import common.enums.UserRole
import common.enums.Visibility
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val password: String = "password",
    val mail: String = "mail@example.com",
    val role: UserRole = UserRole.USER,
    val bio: String = "",
)

@Serializable
data class UserSettingsDTO(
    val autoAcceptFollowRequests: Boolean = false,
    val accountVisibility: Visibility = Visibility.PUBLIC,
)