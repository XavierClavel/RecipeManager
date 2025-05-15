package com.xavierclavel.utils

import kotlinx.serialization.json.Json
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

val json = Json {
    prettyPrint = true
    encodeDefaults = true
}
