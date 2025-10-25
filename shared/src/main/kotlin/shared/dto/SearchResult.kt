package shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult<T>(
    val count: Int,
    val page: Int,
    val size: Int,
    val items: List<T>,
)
