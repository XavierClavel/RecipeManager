package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RecipeEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-recipes"
}


@Serializable
@SerialName("new_recipe")
data class NewRecipeEvent(
    val authorId: String,
    val authorName: String,
    val id: Long,
    val title: String,
    val imageUrl: String
) : RecipeEvent() {
    override fun getKey() = id.toString()
}
