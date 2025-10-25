package shared.dto

import shared.enums.Visibility
import kotlinx.serialization.Serializable

@Serializable
data class CookbookDTO(
    val title: String,
    val description: String = "",
    val visibility: Visibility = Visibility.PUBLIC,
)

@Serializable
data class CookbookUserDTO(
    val id: Long,
    val isAdmin: Boolean,
)