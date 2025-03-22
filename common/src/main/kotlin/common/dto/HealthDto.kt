package common.dto

data class HealthDto(
    val dbStatus: Boolean,
    val version: String,
)