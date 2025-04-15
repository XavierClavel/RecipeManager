package common.infodto

import common.dto.RecipeDTO
import common.enums.DishClass
import common.overviewdto.RecipeOverview
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

    val ingredients: List<RecipeIngredientInfo> = listOf(),
    val customIngredients: List<CustomIngredientInfo> = listOf(),
    val steps: List<String> = listOf(),
    val tips: String = "",

    val creationDate: Long = 0,
    val editionDate: Long? = null,

    val likesCount: Int,

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

    fun toOverview() = RecipeOverview(
        id = this.id,
        title = this.title,
        dishClass = this.dishClass,
        owner = this.owner,
        likesCount = this.likesCount,
        creationDate = this.creationDate,
    )
}