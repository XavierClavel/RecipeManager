package com.xavierclavel.utils

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource


data class Smtp(
    val email: String,
    val password: String,
)

fun loadConfig(): Configuration {
    return ConfigLoaderBuilder.default()
        .addResourceSource("/app/config/configuration.yaml", true)
        .build()
        .loadConfigOrThrow<Configuration>()
        .log { "configuration loaded successfully: $this" }
}



data class Configuration(
    val smtp: Smtp,
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