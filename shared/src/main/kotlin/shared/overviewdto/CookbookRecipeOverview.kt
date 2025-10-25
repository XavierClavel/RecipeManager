package shared.overviewdto

import kotlinx.serialization.Serializable

@Serializable
data class CookbookRecipeOverview(
    val id: Long,
    val title: String,
    val hasRecipe: Boolean,
)