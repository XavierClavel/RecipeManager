package com.xavierclavel.controllers

import com.xavierclavel.services.DashboardService
import com.xavierclavel.services.LikeService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getSessionUserId
import common.utils.URL.DASHBOARD_URL
import common.utils.URL.LIKE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
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