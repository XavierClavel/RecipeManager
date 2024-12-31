package common.dto

import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDTO(
    val name: String,
    val type: IngredientType,

    val calories: Int,
    val glucids: Float = 0f,
    val cholesterol: Float = 0f,
    val lipids: Float = 0f,
    val fibers: Float = 0f,
    val proteins: Float = 0f,
    val sodium: Float = 0f,
)
