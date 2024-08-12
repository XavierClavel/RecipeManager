package fr.xavierclavel.recipemanager
import kotlinx.serialization.*

@Serializable
data class Recipe (
    val title: String,
    /*
    val ingredients: MutableList<Ingredient>,
    val steps: MutableList<String>,
    val portions: Int,
    val cookingTemperature: Int?,
    val cookingTime: Int?,
    val creationDate: Long,
    val editionDate: Long,
     */
)

data class Ingredient (
    val amount: Float,
    val unit: WeightUnit,
    val name: String,
)

enum class WeightUnit {
    UNIT,
    GRAM,
    CUP,
    POUND,
}

