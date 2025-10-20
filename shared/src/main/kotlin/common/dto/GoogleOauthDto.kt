package common.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleOauthDto(
    val sub: String,
    val name: String? = null,
    val given_name: String? = null,
    val family_name: String? = null,
    val picture: String? = null,
    val email: String,
    val email_verified: Boolean? = null
)