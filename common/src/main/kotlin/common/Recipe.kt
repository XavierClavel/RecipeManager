package common

import kotlinx.serialization.*

@Serializable
data class Recipe (
    val title: String,
    val portions: Int,
    val preparationTime: Int?,
    val cookingTime: Int?,
    val cookingTemperature: Int?,

    val ingredients: MutableList<RecipeIngredient>,
    val steps: MutableList<String>,

    var creationDate: Long = 0,
    var editionDate: Long?,

    )

@Serializable
data class RecipeIngredient (
    val name: String,
    val quantity: Quantity?
)

@Serializable
data class Quantity (
    val unit: WeightUnit,
    val name: String,
)

enum class WeightUnit {
    NONE,
    UNIT,
    GRAM,
    CUP,
    POUND,
    TEASPOON,
    TABLESPOON
}

