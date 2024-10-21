package com.xavierclavel.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.healthRoutes() {
    routing {
        healthcheck()
    }
}

fun Route.healthcheck() = get("health") {
    call.respond(mapOf("status" to "UP"))
}