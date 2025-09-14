package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getOptionalSessionId
import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.services.CustomIngredientService
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.RecipeIngredientService
import com.xavierclavel.services.RecipeNotesService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.checkRecipeEditionRights
import com.xavierclavel.utils.getIdPathVariable
import com.xavierclavel.utils.getIdPathVariableSet
import com.xavierclavel.utils.getLocale
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSort
import com.xavierclavel.utils.logger
import common.RecipeFilter
import common.dto.RecipeDTO
import common.enums.DishClass
import common.enums.Locale
import common.utils.URL.RECIPE_NOTES_URL
import common.utils.URL.RECIPE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object RecipeNotesController: Controller(RECIPE_NOTES_URL) {
    val recipeNotesService: RecipeNotesService by inject(RecipeNotesService::class.java)

    override fun Route.routes() {
        getRecipeNotes()
        createRecipeNotes()
        updateRecipeNotes()
        deleteRecipeNotes()
    }

    private fun Route.getRecipeNotes() = get("/{id}") {
        val recipeId = getPathId()
        val userId = getSessionUserId()
        val recipeNotes = recipeNotesService.findNotes(recipeId, userId)
        call.respond(recipeNotes.notes)
    }

    private fun Route.createRecipeNotes() = post("/{id}") {
        val recipeId = getPathId()
        val userId = getSessionUserId()
        val notes = call.receive<String>()
        val recipeNotes = recipeNotesService.createNotes(recipeId, userId, notes)
        call.respond(recipeNotes)
    }

    private fun Route.updateRecipeNotes() = put("/{id}") {
        val recipeId = getPathId()
        val userId = getSessionUserId()
        val notes = call.receive<String>()
        val recipeNotes = recipeNotesService.updateNotes(recipeId, userId, notes)
        call.respond(recipeNotes)
    }

    private fun Route.deleteRecipeNotes() = delete("/{id}") {
        val recipeId = getPathId()
        val userId = getSessionUserId()
        val recipeNotes = recipeNotesService.deleteNotes(recipeId, userId)
        call.respond(recipeNotes)
    }

}