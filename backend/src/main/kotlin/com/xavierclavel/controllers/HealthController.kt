package com.xavierclavel.controllers

import com.xavierclavel.utils.Controller
import common.dto.HealthDto
import common.utils.URL.HEALTH_URL
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.Properties

//TODO after setting up koin
/*
class StatusService : IStatusService {
    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
    }

    override fun getVersion() : String = versionProperties.getProperty("version") ?: "no version"
}
 */

object HealthController: Controller(HEALTH_URL) {
    override fun Route.routes() {
        getHealth()
    }

    private fun Route.getHealth() = get {
        try {
            val versionProperties = Properties()
            versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
            val version = versionProperties.getProperty("version") ?: "no version"
            call.respond(HealthDto(true, version))
        } catch(e: Exception) {
            call.respond(HealthDto(false, "dev"))
        }

    }
}

