package common.dto

import common.enums.AmountUnit
import kotlinx.serialization.*

@Serializable
data class RecipeDTO (
    val title: String,
    val description: String,
    val portions: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,

    val ingredients: MutableList<RecipeIngredientDTO> = mutableListOf(),
    val customIngredients: MutableList<RecipeCustomIngredientDTO> = mutableListOf(),
    val steps: MutableList<String> = mutableListOf(),
)

@Serializable
data class RecipeIngredientDTO (
    val id: Long,
    val unit: AmountUnit,
    val amount: Float,
)

@Serializable
data class RecipeCustomIngredientDTO (
    val name: String,
    val unit: AmountUnit,
    val amount: Float,
)
