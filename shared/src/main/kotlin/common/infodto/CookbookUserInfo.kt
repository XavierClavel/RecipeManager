package common.infodto

import kotlinx.serialization.Serializable

@Serializable
data class CookbookUserInfo(
    val id: Long,
    val username: String,
    val isAdmin: Boolean,
    val joinDate: Long,
)