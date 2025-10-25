package shared.infodto

import kotlinx.serialization.Serializable

@Serializable
data class LikeInfo(
    val recipe: Recipe,
    val user: User,
    val creationDate: Long,
) {
    @Serializable
    data class Recipe(
        val id: Long,
        val title: String,
    )

    @Serializable
    data class User(
        val id: Long,
        val username: String,
    )
}