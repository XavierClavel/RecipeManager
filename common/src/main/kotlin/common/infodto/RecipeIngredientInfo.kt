package common.infodto

import common.enums.AmountUnit
import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientInfo(
    val id: Long,
    val name: String,
    val amount: Float?,
    val unit: AmountUnit,
)