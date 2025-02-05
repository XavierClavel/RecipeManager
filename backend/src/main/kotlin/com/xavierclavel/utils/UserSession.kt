package com.xavierclavel.utils

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val sessionId: String
)
