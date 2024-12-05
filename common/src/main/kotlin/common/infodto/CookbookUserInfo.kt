package common.infodto

import common.enums.CookbookRole
import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class CookbookUserInfo(
    val id: Long,
    val username: String,
    val role: CookbookRole,
    val joinDate: Long,
)