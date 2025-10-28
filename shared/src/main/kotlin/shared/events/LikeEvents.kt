package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class LikeEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-likes"
}


@Serializable
data class RecipeLikedEvent(
    val recipeId: Long,
    val userId: Long
) : LikeEvent() {
    override val type = "recipe_liked"
    override fun getKey() = recipeId.toString()
}

@Serializable
@SerialName("unfollowed_user")
data class RecipeUnlikedEvent(
    val recipeId: Long,
    val userId: Long
) : LikeEvent() {
    override val type = "recipe_unliked"
    override fun getKey() = recipeId.toString()
}
