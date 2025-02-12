package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.controllers.RecipeController.recipeService
import com.xavierclavel.controllers.UserController.imageService
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.services.CookbookService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getIdQueryParam
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSort
import com.xavierclavel.utils.handleDeletion
import com.xavierclavel.utils.logger
import common.dto.CookbookDTO
import common.enums.CookbookRole
import common.utils.Filepath.COOKBOOKS_IMG_PATH
import common.utils.URL.COOKBOOK_URL
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
        listCookbooks()
        updateCookbook()
        deleteCookbook()

        addCookbookUser()
        getCookbookUsers()
        deleteCookbookUser()

        addCookbookRecipe()
        getCookbookRecipes()
        deleteCookbookRecipe()
        getRecipeStatusInUserCookbooks()
    }

    private fun Route.createCookbook() = post {
        val cookbookDTO = call.receive<CookbookDTO>()
        val cookbook = cookbookService.createCookbook(cookbookDTO)
        val userId = getSessionUserId()
        cookbookService.addUserToCookbook(cookbook.id, userId, CookbookRole.OWNER)
        call.respond(HttpStatusCode.Created, cookbook)
    }


    private fun Route.listCookbooks() = get {
        val userId = getIdQueryParam("user")
        val recipeId = getIdQueryParam("recipe")
        val paging = getPaging()
        val sort = getSort()
        val cookbook = cookbookService.listCookbooks(paging, sort, userId)
        call.respond(cookbook)
    }

    private fun Route.getRecipeStatusInUserCookbooks() = get("/recipeStatus") {
        val userId = getIdQueryParam("user") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        val recipeId = getIdQueryParam("recipe") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        val cookbook = cookbookService.getRecipeStatusInUserCookbooks(userId, recipeId)
        call.respond(cookbook)
    }

    private fun Route.getCookbook() = get("/{id}") {
        val id = getPathId()
        val cookbook = cookbookService.getCookbook(id)
        call.respond(cookbook)
    }

    private fun Route.getCookbookUsers() = get("/{id}/users") {
        val id = getPathId()
        val paging = getPaging()
        val users = cookbookService.getCookbookUsers(id, paging)
        call.respond(users)
    }

    private fun Route.getCookbookRecipes() = get("/{id}/recipes") {
        val id = getPathId()
        val paging = getPaging()
        val recipes = cookbookService.getCookbookRecipes(id, paging)
        call.respond(recipes)
    }

    private fun Route.updateCookbook() = put("/{id}") {
        val id = getPathId()
        val cookbookDTO = call.receive<CookbookDTO>()
        val cookbook = cookbookService.updateCookbook(id, cookbookDTO)
        call.respond(cookbook)
    }

    private fun Route.deleteCookbook() = delete("/{id}") {
        val id = getPathId()
        imageService.deleteImage(COOKBOOKS_IMG_PATH, id)
        handleDeletion(cookbookService.deleteCookbook(id))
    }

    private fun Route.addCookbookUser() = post("/{id}/user/{user}") {
        val cookbookId = getPathId()
        val userId = getIdPathVariable("user") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        val role = call.parameters["role"]?.let { CookbookRole.valueOf(it) } ?: CookbookRole.READER
        cookbookService.addUserToCookbook(cookbookId, userId, role)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteCookbookUser() = delete("/{id}/user/{user}") {
        logger.info { "deleting cb user"}
        val cookbookId = getPathId()
        val userId = getIdPathVariable("user") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        handleDeletion(cookbookService.removeUserFromCookbook(cookbookId, userId))
    }

    private fun Route.addCookbookRecipe() = post("/{id}/recipe/{recipe}") {
        val cookbookId = getPathId()
        val recipeId = getIdPathVariable("recipe") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        val userId = getSessionUserId()
        if (cookbookService.doesCookbookHaveRecipe(cookbookId, recipeId)) throw BadRequestException(BadRequestCause.RECIPE_ALREADY_IN_COOKBOOK)
        cookbookService.addRecipeToCookbook(cookbookId, recipeId, userId)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deleteCookbookRecipe() = delete("/{id}/recipe/{recipe}") {
        val cookbookId = getPathId()
        val recipeId = getIdPathVariable("recipe") ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        if (!cookbookService.doesCookbookHaveRecipe(cookbookId, recipeId)) throw BadRequestException(BadRequestCause.RECIPE_NOT_IN_COOKBOOK)
        handleDeletion(cookbookService.removeRecipeFromCookbook(cookbookId, recipeId))
        recipeService.tryDelete(recipeId)
    }

}