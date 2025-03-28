package com.xavierclavel.controllers

import com.xavierclavel.services.ExportService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.logger
import com.xavierclavel.utils.respondPDF
import common.utils.URL.EXPORT_URL
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.java.KoinJavaComponent.inject

object ExportController: Controller(EXPORT_URL) {
    val exportService : ExportService by inject(ExportService::class.java)
    val recipeService : RecipeService by inject(RecipeService::class.java)

    override fun Route.routes() {
        exportRecipe()
        exportUser()
        exportCookbook()
    }

    private fun Route.exportRecipe() = get("/recipe/{id}") {
        val id = getPathId()
        val recipe = recipeService.getEntityById(id).toInfo()
        try {
            val pdfFile = exportService.generatePDF(recipe)
            call.respondPDF("example.pdf", pdfFile)
        } catch (e: Exception) {
            logger.error(e.message)
        }

    }

    private fun Route.exportUser() = get("/user/{id}") {
        val pdfFile = exportService.generatePDF(listOf())
        call.respondPDF("example.pdf", pdfFile)
    }

    private fun Route.exportCookbook() = get("/cookbook/{id}") {
        val pdfFile = exportService.generatePDF(listOf())
        call.respondPDF("example.pdf",pdfFile)
    }

}