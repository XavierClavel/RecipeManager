package common.dto

import common.enums.IngredientType
import common.enums.Locale
import kotlinx.serialization.Serializable

@Serializable
data class IngredientDTO(
    val name: Map<Locale, String> = mapOf(),
    val type: IngredientType = IngredientType.MISCELLANEOUS,

    val calories: Int = 0,
    val carbohydrates: Float = 0f,
    val cholesterol: Float = 0f,
    val saturatedFat: Float = 0f,
    val unsaturatedFat: Float = 0f,
    val fibers: Float = 0f,
    val proteins: Float = 0f,
    val sodium: Float = 0f,
    val sugars: Float = 0f,

    val allowAmount: Boolean = false,
    val allowWeight: Boolean = false,
    val allowVolume: Boolean = false,

    val volumicMass: Float = 1f,
    val weightPerUnit: Float = 1f,
)
