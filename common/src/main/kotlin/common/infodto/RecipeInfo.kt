package common.infodto

import common.dto.RecipeDTO
import common.dto.RecipeIngredient
import kotlinx.serialization.*

@Serializable
data class RecipeInfo (
    val id: Long,
    val title: String,
    val description: String,
    val portions: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,

    val ingredients: List<RecipeIngredient> = listOf(),
    val steps: List<String> = listOf(),

    var creationDate: Long = 0,
    var editionDate: Long? = null,

    ) {
    fun compareToDTO(dto: RecipeDTO): Boolean {
        return title == dto.title &&
                description == dto.description &&
                portions == dto.portions &&
                preparationTime == dto.preparationTime &&
                cookingTime == dto.preparationTime &&
                cookingTemperature == dto.cookingTemperature &&
                steps == dto.steps
    }
}