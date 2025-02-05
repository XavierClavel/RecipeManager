package com.xavierclavel.controllers

import com.xavierclavel.services.ExportService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.logger
import common.utils.URL.EXPORT_URL
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.java.KoinJavaComponent.inject

object ExportController: Controller(EXPORT_URL) {
    val exportService : ExportService by inject(ExportService::class.java)
    val recipeService : RecipeService by inject(RecipeService::class.java)

    override fun Route.routes() {
        exportRecipe()
        exportUser()
        exportCircle()
    }

    private fun Route.exportRecipe() = get("/recipe/{id}") {
        val id = getPathId()
        val recipe = recipeService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Recipe not found")
        try {
            val byteArray = exportService.generatePDF(recipe)
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