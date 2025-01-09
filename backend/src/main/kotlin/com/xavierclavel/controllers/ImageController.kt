package com.xavierclavel.controllers

import com.xavierclavel.controllers.RecipeController.recipeService
import com.xavierclavel.services.ImageService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUsername
import common.utils.Filepath.COOKBOOKS_IMG_PATH
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
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
            default("default.webp")
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.lastModified().toString())
            }
        }

        //Serve static images for users icon
        staticFiles("/users", File(USERS_IMG_PATH)) {
            default("default.webp")
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.lastModified().toString())
            }
        }

        //Serve static images for users icon
        staticFiles("/cookbooks", File(COOKBOOKS_IMG_PATH)) {
            default("default.webp")
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.lastModified().toString())
            }
        }

        uploadRecipeImage()
        uploadCookbookImage()
        uploadUserIcon()
        deleteRecipeImage()
        deleteCookbookImage()
        deleteUserImage()
    }


    private fun Route.uploadRecipeImage() = post("/recipes/{id}") {
        val id = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveImage(RECIPES_IMG_PATH, id, multipartData)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.uploadCookbookImage() = post("/cookbooks/{id}") {
        val id = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveImage(COOKBOOKS_IMG_PATH, id, multipartData)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.uploadUserIcon() = post("/users/{id}") {
        val id = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        val multipartData = call.receiveMultipart()
        imageService.saveImage(USERS_IMG_PATH, id, multipartData)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteRecipeImage() = delete("/recipes/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        if (!isAuthorizedToEditRecipe(id)) return@delete call.respond(HttpStatusCode.Unauthorized)
        imageService.deleteImage(RECIPES_IMG_PATH,id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteCookbookImage() = delete("/users/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        imageService.deleteImage(COOKBOOKS_IMG_PATH, id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteUserImage() = delete("/users/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing ID parameter")
        imageService.deleteImage(USERS_IMG_PATH, id)
        call.respond(HttpStatusCode.OK)
    }


    private fun RoutingContext.isAuthorizedToEditRecipe(recipeId: Long) : Boolean {
        val currentUser = getSessionUsername() ?: return false
        return  recipeService.getRecipeOwner(recipeId) != currentUser
    }


}