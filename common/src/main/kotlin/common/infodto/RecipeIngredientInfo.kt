package common.infodto

import common.enums.AmountUnit
import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientInfo(
    val id: Long,
    val name: String,
    val amount: Float?,
    val unit: AmountUnit,
    val complement : String?,
    val type: IngredientType,
    val allowAmount: Boolean,
    val allowWeight: Boolean,
    val allowVolume: Boolean,
)