package common.dto

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DataPoint(
    val label: String,
    val value: Int,
)

@Serializable
data class WeeklyUserCountDto(
    val weekStart: String,
    val userCount: Int
)