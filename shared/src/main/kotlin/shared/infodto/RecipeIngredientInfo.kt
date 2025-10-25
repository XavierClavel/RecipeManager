package shared.infodto

import shared.enums.AmountUnit
import shared.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredientInfo(
    val id: Long,
    val name: String,
    val amount: Float?,
    val unit: AmountUnit,
    val complement : String?,
    val type: IngredientType,
    val allowAmount: Boolean = true,
    val allowWeight: Boolean = true,
    val allowVolume: Boolean = true,
)