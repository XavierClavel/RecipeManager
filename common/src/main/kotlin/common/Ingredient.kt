package common

data class Ingredient(
    val name: String,
    val type: IngredientType,
    val caloriesPer100g: Int,
    val weightPerUnit: Float,
    val containsSugar: Boolean,
)
