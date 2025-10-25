package shared.infodto

import shared.dto.IngredientDTO
import shared.enums.IngredientType
import shared.enums.Locale
import kotlinx.serialization.Serializable

@Serializable
data class IngredientInfo(
    val id: Long,
    val name: Map<Locale, String>,
    val type: IngredientType,

    val calories: Int = 0,
    val carbohydrates: Float = 0f,
    val cholesterol: Float = 0f,
    val saturatedFat: Float = 0f,
    val unsaturatedFat: Float = 0f,
    val sugars: Float = 0f,
    val fibers: Float = 0f,
    val proteins: Float = 0f,
    val sodium: Float = 0f,

    val allowAmount: Boolean = true,
    val allowWeight: Boolean = true,
    val allowVolume: Boolean = true,

    val volumicMass: Float = 1f,
    val weightPerUnit: Float = 1f,
) {
    fun compareToDTO(ingredientDTO: IngredientDTO): Boolean =
        this.type == ingredientDTO.type &&
        this.name == ingredientDTO.name
}
