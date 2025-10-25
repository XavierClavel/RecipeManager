package com.xavierclavel.controllers

import com.xavierclavel.services.DashboardService
import com.xavierclavel.utils.Controller
import shared.utils.URL.DASHBOARD_URL
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.java.KoinJavaComponent.inject

object DashboardController: Controller(DASHBOARD_URL) {
    val dashboardService : DashboardService by inject(DashboardService::class.java)

    override fun Route.routes() {
        getGeneralReport()
    }

    fun Route.getGeneralReport() = get {
        call.respond(dashboardService.buildGeneralReport())
    }

}