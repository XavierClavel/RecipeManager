package shared.overviewdto

import kotlinx.serialization.Serializable

@Serializable
data class CookbookOverview(
    val id: Long,
    val version: Long,
    val title: String,
)