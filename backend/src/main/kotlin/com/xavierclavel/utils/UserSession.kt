package com.xavierclavel.utils

import io.ktor.server.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val name: String, val count: Int) : Principal
