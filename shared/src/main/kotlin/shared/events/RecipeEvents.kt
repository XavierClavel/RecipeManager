package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("new_recipe")
data class NewRecipeEvent(
    val authorId: String,
    val authorName: String,
    val title: String,
    val imageUrl: String
) : DomainEvent() {
    override val type = "new_recipe"
}
