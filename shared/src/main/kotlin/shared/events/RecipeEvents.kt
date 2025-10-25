package shared.events

import kotlinx.serialization.Serializable


@Serializable
data class NewRecipeEvent(
    val authorId: String,
    val authorName: String,
    val title: String,
    val imageUrl: String
) : DomainEvent() {
    override val type = "new_recipe"
}
