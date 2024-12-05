package common.dto

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class CookbookDTO(
    val title: String,
    val description: String = "",
)