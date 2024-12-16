package common.overviewdto

import kotlinx.serialization.*

@Serializable
data class RecipeOverview (
    val id: Long,
    val title: String,
    val owner: UserOverview,

    val yield: Int? = null,
    val preparationTime: Int? = null,
    val cookingTime: Int? = null,
    val cookingTemperature: Int? = null,
    val conservationTime: Int? = null,

    var creationDate: Long = 0,

    var likesCount: Int,
)
