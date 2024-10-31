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
        listIngredients()
    }

    private fun Route.createIngredient() = post {
        val ingredientDTO = call.receive<IngredientDTO>()
        ingredientService.createIngredient(ingredientDTO)
        call.respond(HttpStatusCode.Created)
    }

//    private fun Route.editUser() = put {
//        val userDTO = call.receive<UserDTO>()
//        val user = userService.findByUsername(userDTO.username) ?: return@put call.respond(HttpStatusCode.BadRequest)
//        //userService.editUser(user, userDTO)
//        call.respond(HttpStatusCode.OK)
//    }

    private fun Route.listIngredients() = get {
        val searchString = call.request.queryParameters["search"] ?: ""
        call.respond(ingredientService.search(searchString, getPaging()))
    }

//    private fun Route.getUser() = get("/{username}") {
//        val username = call.parameters["username"]!!
//        val user = userService.getUserByUsername(username) ?: return@get call.respond(HttpStatusCode.NotFound)
//        call.respond(user)
//    }

//    private fun Route.deleteUser() = delete("/{username}") {
//        val username = call.parameters["username"]!!
//        userService.deleteUserByUsername(username)
//        call.respond(HttpStatusCode.OK)
//    }
}