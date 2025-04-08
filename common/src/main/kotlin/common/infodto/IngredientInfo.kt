package common.infodto

import common.dto.IngredientDTO
import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientInfo(
    val id: Long,
    val name: String,
    val type: IngredientType,

    val calories: Int = 0,
    val glucids: Float = 0f,
    val cholesterol: Float = 0f,
    val lipids: Float = 0f,
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
        this.name == ingredientDTO.name &&
        this.type == ingredientDTO.type
}
