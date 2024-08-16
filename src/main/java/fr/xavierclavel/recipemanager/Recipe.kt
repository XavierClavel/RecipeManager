package fr.xavierclavel.recipemanager
import kotlinx.serialization.*

@Serializable
data class Recipe (
    val title: String,
    val portions: Int,
    //val cookingTemperature: Int?,
    //val cookingTime: Int?,

    //val ingredients: MutableList<Ingredient>,
    val steps: MutableList<String>,

    var creationDate: Long = 0,
    var editionDate: Long?,

    ) {
    constructor(title: String, portions: Int, steps: MutableList<String>) : this(
        title,
        portions,
        steps,
        0,
        null,
        )
}

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

