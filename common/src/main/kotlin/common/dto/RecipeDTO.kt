package common.dto

import common.enums.AmountUnit
import kotlinx.serialization.*

@Serializable
data class RecipeDTO (
    val title: String,
    val description: String = "",
    val portions: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,

    val ingredients: MutableList<RecipeIngredient> = mutableListOf(),
    val steps: MutableList<String> = mutableListOf(),
)

@Serializable
data class RecipeIngredient (
    val name: String,
    val unit: AmountUnit,
    val amount: Float,
)
