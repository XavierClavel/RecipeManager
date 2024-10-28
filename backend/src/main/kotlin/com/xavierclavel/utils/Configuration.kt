package com.xavierclavel.utils

import org.koin.core.component.KoinComponent


data class Configuration(
    val database: Database,
)

data class Database(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
)

data class Authentication(
    val adminUser: String,
    val adminPassword: String,
)