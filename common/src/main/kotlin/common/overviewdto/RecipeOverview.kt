package common.overviewdto

import common.enums.DishClass
import kotlinx.serialization.*

@Serializable
data class RecipeOverview(
    val id: Long,
    val title: String,
    val dishClass: DishClass,
    val owner: UserOverview,
    val likesCount: Int,
    val creationDate: Long
)