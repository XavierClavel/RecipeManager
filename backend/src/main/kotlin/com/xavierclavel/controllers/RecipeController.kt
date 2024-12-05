package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUsername
import com.xavierclavel.utils.logger
import common.infodto.RecipeInfo
import common.utils.URL.RECIPE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object RecipeController: Controller(RECIPE_URL) {
    val recipeService: RecipeService by inject(RecipeService::class.java)
    val userService: UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)

    override fun Route.routes() {
        getRecipe()
        listRecipes()
        getUserRecipesCount()
        createRecipe()
        updateRecipe()
        deleteRecipe()
    }

    private fun Route.getRecipe() = get("/{id}") {
        val recipeId = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val recipe = recipeService.findById(recipeId) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(recipe)
    }

    private fun Route.listRecipes() = get {
        val paging = getPaging()
        val owner = call.request.queryParameters["owner"]?.toLongOrNull()
        val likedBy = call.request.queryParameters["likedBy"]?.toLongOrNull()
        val cookbook = call.request.queryParameters["cookbook"]?.toLongOrNull()
        call.respond(recipeService.findList(paging, owner, likedBy, cookbook))
    }

    private fun Route.createRecipe() = post {
        try {
            val username = getSessionUsername() ?: return@post call.respond(HttpStatusCode.Unauthorized)
            val user = userService.findByUsername(username) ?: return@post call.respond(HttpStatusCode.Unauthorized)
            call.respond(HttpStatusCode.Created, recipeService.createRecipe(call.receive(), user))
        } catch (e: Exception) {
            logger.error {e.message}
        }

    }

    private fun Route.updateRecipe() = put("/{id}") {
        try {
            val recipeId = getPathId() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val recipe = recipeService.findById(recipeId) ?: return@put call.respond(HttpStatusCode.NotFound)
            if (!isAuthorizedToEditRecipe(recipe)) return@put call.respond(HttpStatusCode.Unauthorized)
            val info =
                recipeService.updateRecipe(recipeId, call.receive()) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, info)
        } catch (e: Exception) {
            logger.error {e.message}
        }
    }

    private fun Route.deleteRecipe() = delete("/{id}") {
        val recipeId = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val recipe = recipeService.findById(recipeId) ?: return@delete call.respond(HttpStatusCode.NotFound)
        if (!isAuthorizedToEditRecipe(recipe)) return@delete call.respond(HttpStatusCode.Unauthorized)
        recipeService.deleteRecipe(recipeId)
        imageService.deleteRecipeImage(recipeId)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.getUserRecipesCount() = get("/user/{username}/count") {
        val username = call.parameters["username"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val recipes = recipeService.countByOwner(username)
        call.respond(recipes)
    }

    private fun RoutingContext.isAuthorizedToEditRecipe(recipe: RecipeInfo): Boolean {
        val currentUser = getSessionUsername() ?: return false
        return recipe.owner == currentUser
    }



}