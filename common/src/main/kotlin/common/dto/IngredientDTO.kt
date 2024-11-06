package common.dto

import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDTO(
    val name: String,
    val type: IngredientType,
    val calories: Int,
    //val weightPerUnit: Float,
    //val containsSugar: Boolean,
)
