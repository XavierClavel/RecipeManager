package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.controllers.RecipeController.recipeService
import com.xavierclavel.exceptions.ForbiddenCause
import com.xavierclavel.exceptions.ForbiddenException
import com.xavierclavel.services.ImageService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.checkRecipeEditionRights
import com.xavierclavel.utils.checkUserEditionRights
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.receiveImage
import common.utils.Filepath.COOKBOOKS_IMG_PATH
import common.utils.Filepath.RECIPES_IMG_PATH
import common.utils.Filepath.RECIPES_THUMBNAIL_PATH
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.IMAGE_URL
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject
import java.io.File

object ImageController: Controller(IMAGE_URL) {
    val imageService : ImageService by inject(ImageService::class.java)

    /*
        Recipe full display : 800x600
        Recipe icon display : 240x180
        User icon display : 200x200
        Cookbook icon display : 250x250
     */
    val SIZE_IMG_USER = Pair(400,400)
    val SIZE_IMG_RECIPE = Pair(1600,1200)
    val SIZE_THUMBNAIL_RECIPE = Pair(480,360)
    val SIZE_IMG_COOKBOOK = Pair(500,500)

    override fun Route.routes() {
        //Serve static images for recipes
        staticFiles("/recipes", File(RECIPES_IMG_PATH)) {
            default("default.webp")
            modify { file, call ->
                call.response.headers.append(HttpHeaders.ETag, file.lastModified().toString())
            }
        }

        //Serve static images for recipes
        staticFiles("/recipes-thumbnails", File(RECIPES_THUMBNAIL_PATH)) {
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
        val id = getPathId()
        checkRecipeEditionRights(recipeService.getRecipeOwner(id).id)
        val image = receiveImage()
        imageService.saveImage(RECIPES_IMG_PATH, id, SIZE_IMG_RECIPE, image)
        imageService.saveImage(RECIPES_THUMBNAIL_PATH, id, SIZE_THUMBNAIL_RECIPE, image)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.uploadCookbookImage() = post("/cookbooks/{id}") {
        val id = getPathId()
        val image = receiveImage()
        imageService.saveImage(COOKBOOKS_IMG_PATH, id, SIZE_IMG_COOKBOOK, image)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.uploadUserIcon() = post("/users/{id}") {
        val id = getPathId()
        checkUserEditionRights(id)
        val image = receiveImage()
        imageService.saveImage(USERS_IMG_PATH, id, SIZE_IMG_USER, image)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteRecipeImage() = delete("/recipes/{id}") {
        val id = getPathId()
        checkRecipeEditionRights(recipeService.getRecipeOwner(id).id)
        imageService.deleteImage(RECIPES_IMG_PATH,id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteCookbookImage() = delete("/users/{id}") {
        val id = getPathId()
        imageService.deleteImage(COOKBOOKS_IMG_PATH, id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteUserImage() = delete("/users/{id}") {
        val id = getPathId()
        checkUserEditionRights(id)
        imageService.deleteImage(USERS_IMG_PATH, id)
        call.respond(HttpStatusCode.OK)
    }





}