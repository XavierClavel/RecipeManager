package com.xavierclavel.controllers

import com.xavierclavel.services.CookbookService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getIdQueryParam
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import com.xavierclavel.utils.handleDeletion
import com.xavierclavel.utils.logger
import common.dto.CookbookDTO
import common.enums.CookbookRole
import common.utils.URL.COOKBOOK_URL
import common.utils.URL.LIKE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object CookbookController: Controller(COOKBOOK_URL) {
    val cookbookService : CookbookService by inject(CookbookService::class.java)

    override fun Route.routes() {
        createCookbook()
        getCookbook()
        updateCookbook()
        deleteCookbook()

        addCookbookUser()
        getCookbookUsers()
        deleteCookbookUser()

        addCookbookRecipe()
        deleteCookbookRecipe()
    }

    private fun Route.createCookbook() = post {
        val cookbookDTO = call.receive<CookbookDTO>()
        val cookbook = cookbookService.createCookbook(cookbookDTO)
        val userId = getSessionUserId() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        cookbookService.addUserToCookbook(cookbook.id, userId, CookbookRole.OWNER)
        call.respond(HttpStatusCode.Created, cookbook)
    }

    private fun Route.getCookbook() = get("/{id}") {
        val id = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val cookbook = cookbookService.getCookbook(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(cookbook)
    }

    private fun Route.getCookbookUsers() = get("/{id}/users") {
        val id = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val paging = getPaging()
        call.respond(cookbookService.getCookbookUsers(id, paging))
    }

    private fun Route.updateCookbook() = put("/{id}") {
        val id = getPathId() ?: return@put call.respond(HttpStatusCode.BadRequest)
        val cookbookDTO = call.receive<CookbookDTO>()
        val cookbook = cookbookService.updateCookbook(id, cookbookDTO) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(cookbook)
    }

    private fun Route.deleteCookbook() = delete("/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        handleDeletion(cookbookService.deleteCookbook(id))
    }

    private fun Route.addCookbookUser() = post("/{id}/user/{user}") {
        val cookbookId = getPathId() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val userId = getIdQueryParam("user") ?: return@post call.respond(HttpStatusCode.BadRequest)
        val role = call.parameters["role"]?.let { CookbookRole.valueOf(it) } ?: CookbookRole.READER
        cookbookService.addUserToCookbook(cookbookId, userId, role)
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