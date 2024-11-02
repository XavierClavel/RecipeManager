package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.utils.Controller
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject
import java.io.File

object ImageController: Controller(IMAGE_URL) {
    val imageService : ImageService by inject(ImageService::class.java)

    override fun Route.routes() {
        staticFiles("/recipes", File(RECIPES_IMG_PATH)) {
            default("index.html")
            preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.name.toString())
            }
        }
        uploadRecipeImage()
    }


    private fun Route.uploadRecipeImage() = post("/recipes/{id}") {
        val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveRecipeImage(id, multipartData)
        call.respond(HttpStatusCode.OK)
    }



}