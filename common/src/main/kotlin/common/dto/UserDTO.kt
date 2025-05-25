package common.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    var username: String,
    val password: String? = null,
    val googleId: String? = null,
    val mail: String = "mail@example.com",
    val bio: String = "",
)

@Serializable
data class UserSettingsDTO(
    val autoAcceptFollowRequests: Boolean = false,
    val isAccountPublic: Boolean = false,
)