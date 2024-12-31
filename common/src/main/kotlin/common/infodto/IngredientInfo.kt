package common.infodto

import common.dto.IngredientDTO
import common.enums.IngredientType
import kotlinx.serialization.Serializable

@Serializable
data class IngredientInfo(
    val id: Long,
    val name: String,
    val type: IngredientType,
) {
    fun compareToDTO(ingredientDTO: IngredientDTO): Boolean =
        this.name == ingredientDTO.name &&
        this.type == ingredientDTO.type
}
