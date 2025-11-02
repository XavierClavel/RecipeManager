package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class LikeEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-likes"
}


@Serializable
@SerialName("recipe_liked")
data class RecipeLikedEvent(
    val recipeId: Long,
    val userId: Long
) : LikeEvent() {
    override fun getKey() = recipeId.toString()
}

@Serializable
@SerialName("recipe_unliked")
data class RecipeUnlikedEvent(
    val recipeId: Long,
    val userId: Long
) : LikeEvent() {
    override fun getKey() = recipeId.toString()
}
