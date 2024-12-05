package com.xavierclavel.controllers

import com.xavierclavel.services.CookbookService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import com.xavierclavel.utils.handleDeletion
import common.dto.CookbookDTO
import common.enums.CookbookRole
import common.utils.URL.LIKE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject

object CookbookController: Controller(LIKE_URL) {
    val cookbookService : CookbookService by inject(CookbookService::class.java)

    override fun Route.routes() {
        createCookbook()
        deleteCookbook()
        addCookbookUser()
        deleteCookbookUser()
        addCookbookRecipe()
        deleteCookbookRecipe()
    }

    private fun Route.createCookbook() = post {
        val cookbookDTO = call.receive<CookbookDTO>()
        call.respond(cookbookService.createCookbook(cookbookDTO))
    }

    private fun Route.deleteCookbook() = delete("/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        handleDeletion(cookbookService.deleteCookbook(id))
    }

    private fun Route.addCookbookUser() = post("/{id}/user/{user}") {
        val cookbookId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = call.parameters["user"]?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val role = call.parameters["role"]?.let { CookbookRole.valueOf(it) } ?: return@post call.respond(HttpStatusCode.BadRequest)
        cookbookService.addUserToCookbook(userId, cookbookId, role)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteCookbookUser() = post("/{id}/user/{user}") {
        val cookbookId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = call.parameters["user"]?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
        handleDeletion(cookbookService.removeUserFromCookbook(cookbookId, userId))
    }

    private fun Route.addCookbookRecipe() = post("/{id}/recipe/{recipe}") {
        val cookbookId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val recipeId = call.parameters["recipe"]?.toLongOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = getSessionUserId() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        cookbookService.addRecipeToCookbook(cookbookId, recipeId, userId)
    }

    private fun Route.deleteCookbookRecipe() = delete("/{id}/recipe/{recipe}") {
        val cookbookId = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val recipeId = call.parameters["recipe"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        handleDeletion(cookbookService.removeRecipeFromCookbook(cookbookId, recipeId))
    }

}