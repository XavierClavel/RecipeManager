package common.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordDTO(
    val old: String,
    val new: String,
)