package common.infodto

import common.dto.RecipeDTO
import common.enums.AmountUnit
import kotlinx.serialization.Serializable

@Serializable
data class CustomIngredientInfo(
    val name: String,
    val amount: Float?,
    val unit: AmountUnit,
) {
    companion object {
        fun fromDTO(dto: RecipeDTO.CustomIngredientDTO) = CustomIngredientInfo(
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit,
        )
    }

}