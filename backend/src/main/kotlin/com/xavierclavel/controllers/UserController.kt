package com.xavierclavel.controllers

import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import common.dto.UserDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject

object UserController: Controller("v1/user") {
    val userService : UserService by inject(UserService::class.java)

    override fun Route.routes() {
        createUser()
        getUser()
    }

    private fun Route.createUser() = post() {
        val userDTO = call.receive<UserDTO>()
    }

    private fun Route.getUser() = get("/{id}") {
        val id = call.parameters["id"]!!.toLong()
        val user = userService.getUser(id)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "User does not exist")
            return@get
        }
        else call.respond(user)
    }
}