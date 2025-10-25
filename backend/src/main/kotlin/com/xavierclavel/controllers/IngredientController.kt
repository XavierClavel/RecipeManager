package com.xavierclavel.controllers

import com.xavierclavel.services.IngredientService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getLocale
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getQuery
import com.xavierclavel.utils.json
import shared.dto.IngredientDTO
import shared.dto.SearchResult
import shared.infodto.IngredientInfo
import shared.utils.URL.INGREDIENT_URL
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

object IngredientController: Controller(INGREDIENT_URL) {
    val ingredientService : IngredientService by inject(IngredientService::class.java)

    override fun Route.routes() {
        getIngredient()
        searchIngredients()
        getCount()
        getRecipesCount()

        authenticate("admin-session") {
            createIngredient()
            createIngredientsBatch()
            updateIngredient()
            deleteIngredient()
        }
    }

    private fun Route.createIngredient() = post {
        val ingredientDTO = call.receive<IngredientDTO>()
        call.respond(HttpStatusCode.Created, ingredientService.createIngredient(ingredientDTO))
    }

    private fun Route.createIngredientsBatch() = post("/batch") {
        val ingredientsDTO = call.receive<List<IngredientDTO>>()
        for (ingredient in ingredientsDTO) {
            ingredientService.createIngredient(ingredient)
        }
        call.respond(HttpStatusCode.Created)
    }

    private fun Route.updateIngredient() = put("/{id}") {
        val id = getPathId()
        val ingredientDTO = call.receive<IngredientDTO>()
        val ingredient = ingredientService.updateIngredient(id, ingredientDTO)
        call.respond(ingredient)
    }

    private fun Route.deleteIngredient() = delete("/{id}") {
        val id = getPathId()
        val result = ingredientService.deleteById(id)
        return@delete if (result) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
    }

    private fun Route.searchIngredients() = get {
        val searchString = getQuery()
        val paging = getPaging()
        val ingredients = ingredientService.search(searchString, paging, getLocale())
        val result = SearchResult(ingredients.first, paging.pageIndex(), paging.pageSize(), ingredients.second)
        call.respond(json.encodeToString(SearchResult.serializer(IngredientInfo.serializer()), result))
    }

    private fun Route.getIngredient() = get("/{id}") {
        val id = getPathId()
        val result = ingredientService.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(result)
    }

    private fun Route.getCount() = get("/count") {
        call.respond(ingredientService.countAll())
    }

    private fun Route.getRecipesCount() = get("/count/recipes/{id}") {
        call.respond(ingredientService.countRecipes(getPathId()))
    }

}