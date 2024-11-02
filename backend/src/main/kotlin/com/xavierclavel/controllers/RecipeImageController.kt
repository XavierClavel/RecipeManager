package com.xavierclavel.controllers

import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getSessionUsername
import com.xavierclavel.utils.logger
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.files
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.receive
import io.ktor.server.request.receiveChannel
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeBytes

object RecipeImageController: Controller(IMAGE_URL) {
    val recipeService: RecipeService by inject(RecipeService::class.java)
    val userService: UserService by inject(UserService::class.java)

    const val path = "/img/recipes"

    override fun Route.routes() {
        staticFiles("/recipes", File(path)) {
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
        val file = Path("/img/recipes/${id}.png")
        file.createParentDirectories()
        if (!file.exists()) {
            file.createFile()
        }

        logger.info {"here"}

        var fileReceived = false
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    logger.info {"receiving multipart"}
                    fileReceived = true
                    val fileBytes = part.provider().readRemaining().readByteArray()
                    logger.info {"receiving multipart"}
                    try {
                        file.writeBytes(fileBytes)
                    } catch (e: Exception) {
                        logger.error {e.message}
                    }

                    logger.info {"receiving multipart"}
                }
                else -> {
                    logger.error {"unexpected form data"}
                    call.respond(HttpStatusCode.BadRequest, "Unexpected form data")
                }
            }
            part.dispose()
        }
        logger.info {"here2"}
        if (fileReceived) {
            logger.info {"file import successful"}
            call.respond(HttpStatusCode.OK)
        } else {
            logger.error {"file data not received"}
            call.respond(HttpStatusCode.BadRequest, "File data not received")
        }
    }

}