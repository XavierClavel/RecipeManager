package common.dto

import common.enums.AmountUnit
import common.enums.DishClass
import common.infodto.CustomIngredientInfo
import kotlinx.serialization.*

@Serializable
data class RecipeDTO (
    val title: String,
    val description: String = "",
    val dishClass: DishClass = DishClass.MAIN_DISH,

    val yield: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,

    val ingredients: MutableList<RecipeIngredientDTO> = mutableListOf(),
    val customIngredients: MutableList<CustomIngredientDTO> = mutableListOf(),
    val steps: MutableList<String> = mutableListOf(),
) {
    @Serializable
    data class RecipeIngredientDTO (
        val id: Long,
        val unit: AmountUnit = AmountUnit.NONE,
        val amount: Float? = null,
        val complement: String? = null,
    )

    @Serializable
    data class CustomIngredientDTO (
        val name: String,
        val unit: AmountUnit = AmountUnit.NONE,
        val amount: Float? = null,
    ) {
        fun toInfo() = CustomIngredientInfo(
            name = name,
            unit = unit,
            amount = amount,
        )
    }
}


