package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getSessionUsername
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
        getUserRecipes()
        getCircleRecipes()
        createRecipe()
        updateRecipe()
        deleteRecipe()
    }

    private fun Route.getRecipe() = get("/{recipe}") {
        val recipeId = call.parameters["recipe"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val recipe = recipeService.findById(recipeId) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(recipe)
    }

    private fun Route.listRecipes() = get {
        call.respond(recipeService.findList())
    }

    private fun Route.createRecipe() = post {
        val username = getSessionUsername() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        val user = userService.findByUsername(username) ?: return@post call.respond(HttpStatusCode.Unauthorized)
        call.respond(HttpStatusCode.Created, recipeService.createRecipe(call.receive(), user))
    }

    private fun Route.updateRecipe() = put("/{recipe}") {
        val recipeId = call.parameters["recipe"]?.toLongOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
        if (!isAuthorizedToEditRecipe(recipeId)) return@put call.respond(HttpStatusCode.Unauthorized)
        val info = recipeService.updateRecipe(recipeId, call.receive()) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(HttpStatusCode.OK, info)
    }

    private fun Route.deleteRecipe() = delete("/{recipe}") {
        val recipeId = call.parameters["recipe"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        if (!isAuthorizedToEditRecipe(recipeId)) return@delete call.respond(HttpStatusCode.Unauthorized)
        recipeService.deleteRecipe(recipeId)
        imageService.deleteRecipeImage(recipeId)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.getUserRecipes() = get("/user/{username}") {
        val username = call.parameters["username"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val recipes = recipeService.findAllByOwnerUsername(username)
        call.respond(recipes)
    }

    private fun Route.getCircleRecipes() = get("/circle/{circleId}") {
        val circleId = call.parameters["circleId"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val recipes = recipeService.findAllByCircleId(circleId)
        call.respond(recipes)
    }

    private fun RoutingContext.isAuthorizedToEditRecipe(recipeId: Long): Boolean {
        val currentUser = getSessionUsername() ?: return false
        return recipeService.getRecipeOwner(recipeId) == currentUser
    }



}