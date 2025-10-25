package shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class HealthDto(
    val dbStatus: Boolean,
    val version: String,
)