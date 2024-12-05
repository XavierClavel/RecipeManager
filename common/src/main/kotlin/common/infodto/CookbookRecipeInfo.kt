package common.infodto

import common.enums.CookbookRole
import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class CookbookRecipeInfo(
    val id: Long,
    val title: String,
    val addedById: Long,
    val addedByUsername: String,
    val additionDate: Long,
)