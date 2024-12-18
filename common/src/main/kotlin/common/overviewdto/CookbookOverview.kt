package common.overviewdto

import kotlinx.serialization.Serializable

@Serializable
data class CookbookOverview(
    val id: Long,
    val title: String,
)