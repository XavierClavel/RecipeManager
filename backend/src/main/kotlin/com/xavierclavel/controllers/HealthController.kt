package com.xavierclavel.controllers

import com.xavierclavel.utils.Controller
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

object HealthController: Controller("v1/health") {
    override fun Route.routes() {
        getHealth()
    }

    private fun Route.getHealth() = get("health") {
        val versionProperties = Properties()
        versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
        val version = versionProperties.getProperty("version") ?: "no version"
        call.respond(mapOf(
            "dbUp" to true,
            "version" to version,
        ))
    }
}

