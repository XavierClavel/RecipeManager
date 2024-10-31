package common.dto

import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDTO(
    val name: String,
    val type: IngredientType,
    //val caloriesPer100g: Int,
    //val weightPerUnit: Float,
    //val containsSugar: Boolean,
)
