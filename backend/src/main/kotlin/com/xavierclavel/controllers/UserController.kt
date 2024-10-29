package com.xavierclavel.controllers

import com.xavierclavel.services.UserService
import com.xavierclavel.utils.AuthenticatedController
import com.xavierclavel.utils.Controller
import common.dto.UserDTO
import common.utils.URL.USER_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object UserController: AuthenticatedController(USER_URL) {
    val userService : UserService by inject(UserService::class.java)

    override fun Route.routes() {
        createUser()
        editUser()
        getUser()
        listUsers()
        deleteUser()
    }

    private fun Route.createUser() = post {
        val userDTO = call.receive<UserDTO>()
        userService.createUser(userDTO)
        call.respond(HttpStatusCode.Created)
    }

    private fun Route.editUser() = put {
        val userDTO = call.receive<UserDTO>()
        val user = userService.findByUsername(userDTO.username) ?: return@put call.respond(HttpStatusCode.BadRequest)
        userService.editUser(user, userDTO)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.listUsers() = get {
        call.respond(userService.listUsers())
    }

    private fun Route.getUser() = get("/{username}") {
        val username = call.parameters["username"]!!
        val user = userService.getUserByUsername(username) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

    private fun Route.deleteUser() = delete("/{username}") {
        val username = call.parameters["username"]!!
        userService.deleteUserByUsername(username)
        call.respond(HttpStatusCode.OK)
    }
}