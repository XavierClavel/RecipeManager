package common.infodto

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class CookbookInfo(
    val id: Long,
    val title: String,
    val description: String = "",
)