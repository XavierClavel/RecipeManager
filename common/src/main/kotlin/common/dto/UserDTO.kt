package common.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val username: String,
    val password: String = "password",
    val mail: String = "mail@example.com",
    val bio: String = "",
)

@Serializable
data class UserSettingsDTO(
    val autoAcceptFollowRequests: Boolean = false,
    val isAccountPublic: Boolean = false,
)