package com.xavierclavel.controllers

import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getSessionUsername
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.files
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.receive
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import org.koin.java.KoinJavaComponent.inject
import java.io.File

object RecipeImageController: Controller(IMAGE_URL) {
    val recipeService: RecipeService by inject(RecipeService::class.java)
    val userService: UserService by inject(UserService::class.java)

    override fun Route.routes() {
        staticFiles("/recipes", File("recipes")) {
            default("index.html")
            preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.name.toString())
            }
        }
    }


    private fun Route.uploadRecipeImage() = get("/recipes/{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val file = File("/img/recipes/${id}.png")
        call.receiveChannel().copyAndClose(file.writeChannel())
        call.respond(HttpStatusCode.OK)
    }

}