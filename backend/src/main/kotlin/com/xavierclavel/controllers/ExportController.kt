package com.xavierclavel.controllers

import com.xavierclavel.services.ExportService
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.utils.URL.EXPORT_URL
import common.utils.URL.USER_URL
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object ExportController: Controller(EXPORT_URL) {
    val exportService : ExportService by inject(ExportService::class.java)

    override fun Route.routes() {
        exportRecipe()
        exportUser()
        exportCircle()
    }

    private fun Route.exportRecipe() = get("/recipe/{id}") {
        try {
            val byteArray = exportService.generatePDF(listOf())
            val filename = "example.pdf"
            call.response.header("Content-Disposition", "attachment; filename=\"$filename\"")
            call.respondBytes(byteArray, contentType = ContentType.Application.Pdf)
        } catch (e: Exception) {
            logger.error(e.message)
        }

    }

    private fun Route.exportUser() = get("/user/{id}") {
        val byteArray = exportService.generatePDF(listOf())
        val filename = "example.pdf"
        call.response.header("Content-Disposition", "attachment; filename=\"$filename\"")
        call.respondBytes(byteArray, contentType = ContentType.Application.Pdf)
    }

    private fun Route.exportCircle() = get("/circle/{id}") {
        val byteArray = exportService.generatePDF(listOf())
        val filename = "example.pdf"
        call.response.header("Content-Disposition", "attachment; filename=\"$filename\"")
        call.respondBytes(byteArray, contentType = ContentType.Application.Pdf)
    }

}