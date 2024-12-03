package com.xavierclavel.utils

import common.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val username: String, val role: UserRole, val id: Long)
