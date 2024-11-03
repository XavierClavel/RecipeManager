package com.xavierclavel.controllers

import com.xavierclavel.controllers.RecipeController.recipeService
import com.xavierclavel.services.ImageService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getSessionUsername
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.CompressedFileType
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject
import java.io.File

object ImageController: Controller(IMAGE_URL) {
    val imageService : ImageService by inject(ImageService::class.java)

    override fun Route.routes() {
        //Serve static images for recipes
        staticFiles("/recipes", File(RECIPES_IMG_PATH)) {
            default("index.html")
            preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.name.toString())
            }
        }

        //Serve static images for users icon
        staticFiles("/users", File(USERS_IMG_PATH)) {
            default("index.html")
            preCompressed(CompressedFileType.BROTLI, CompressedFileType.GZIP)
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.name.toString())
            }
        }

        uploadRecipeImage()
        uploadUserIcon()
        deleteRecipeImage()
        deleteUserImage()
    }


    private fun Route.uploadRecipeImage() = post("/recipes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveRecipeImage(id, multipartData)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.uploadUserIcon() = post("/users/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveUserImage(id, multipartData)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteRecipeImage() = delete("/recipes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        if (!isAuthorizedToEditRecipe(id)) return@delete call.respond(HttpStatusCode.Unauthorized)
        imageService.deleteRecipeImage(id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteUserImage() = delete("/users/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        imageService.deleteUserImage(id)
        call.respond(HttpStatusCode.OK)
    }


    private fun RoutingContext.isAuthorizedToEditRecipe(recipeId: Long) : Boolean {
        val currentUser = getSessionUsername() ?: return false
        return  recipeService.getRecipeOwner(recipeId) != currentUser
    }


}