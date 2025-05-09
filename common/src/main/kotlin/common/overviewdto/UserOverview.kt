package common.overviewdto

import kotlinx.serialization.Serializable

@Serializable
data class UserOverview(
    val id: Long,
    val version: Long,
    val username: String,
)