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

    val ingredients: MutableList<RecipeIngredient> = mutableListOf(),
    val customIngredients: MutableList<RecipeCustomIngredient> = mutableListOf(),
    val steps: MutableList<String> = mutableListOf(),
)

@Serializable
data class RecipeIngredient (
    val id: Long,
    val unit: AmountUnit,
    val amount: Float,
)

@Serializable
data class RecipeCustomIngredient (
    val name: String,
    val unit: AmountUnit,
    val amount: Float,
)
