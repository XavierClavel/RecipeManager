package common.infodto

import common.enums.AmountUnit
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCustomIngredientInfo(
    val name: String,
    val amount: Float,
    val unit: AmountUnit,
)