package common.infodto

import common.dto.RecipeDTO
import common.enums.DishClass
import common.overviewdto.UserOverview
import kotlinx.serialization.*

@Serializable
data class RecipeInfo (
    val id: Long,
    val title: String,
    val dishClass: DishClass,
    val owner: UserOverview,
    val description: String,

    val yield: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,
    val conservationTime: Int? = null,

    val ingredients: List<RecipeIngredientInfo> = listOf(),
    val customIngredients: List<CustomIngredientInfo> = listOf(),
    val steps: List<String> = listOf(),
    val tips: List<String> = listOf(),

    var creationDate: Long = 0,
    var editionDate: Long? = null,

    var likesCount: Int,

    ) {
    fun compareToDTO(dto: RecipeDTO): Boolean {
        return title == dto.title &&
                description == dto.description &&
                yield == dto.yield &&
                preparationTime == dto.preparationTime &&
                cookingTime == dto.preparationTime &&
                cookingTemperature == dto.cookingTemperature &&
                steps == dto.steps
    }
}