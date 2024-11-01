package com.xavierclavel.controllers

import com.xavierclavel.services.IngredientService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import common.dto.IngredientDTO
import common.dto.UserDTO
import common.utils.URL.INGREDIENT_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object IngredientController: Controller(INGREDIENT_URL) {
    val ingredientService : IngredientService by inject(IngredientService::class.java)

    override fun Route.routes() {
        createIngredient()
        getIngredient()
        updateIngredient()
        listIngredients()
        deleteIngredient()
    }

    private fun Route.createIngredient() = post {
        val ingredientDTO = call.receive<IngredientDTO>()
        call.respond(HttpStatusCode.Created, ingredientService.createIngredient(ingredientDTO))
    }

    private fun Route.updateIngredient() = put("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
        val ingredientDTO = call.receive<IngredientDTO>()
        val ingredient = ingredientService.updateIngredient(id, ingredientDTO) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(ingredient)
    }

    private fun Route.deleteIngredient() = delete("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val result = ingredientService.deleteById(id)
        return@delete if (result) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
    }

    private fun Route.listIngredients() = get {
        val searchString = call.request.queryParameters["search"] ?: ""
        call.respond(ingredientService.search(searchString, getPaging()))
    }

    private fun Route.getIngredient() = get("/{id}") {
        val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val result = ingredientService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(result)
    }

}