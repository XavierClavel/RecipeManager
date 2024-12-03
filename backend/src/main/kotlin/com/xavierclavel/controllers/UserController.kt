package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.logger
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

object UserController: Controller(USER_URL) {
    val userService : UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)

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

    private fun Route.editUser() = put("/{id}") {
        val id = getPathId() ?: return@put call.respond(HttpStatusCode.BadRequest)
        val userDTO = call.receive<UserDTO>()
        val response = userService.editUser(id, userDTO) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(response)
    }

    private fun Route.listUsers() = get {
        call.respond(userService.listUsers())
    }

    private fun Route.getUser() = get("/{id}") {
        val id = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

    private fun Route.deleteUser() = delete("/{id}") {
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(id) ?: return@delete call.respond(HttpStatusCode.NotFound)
        userService.deleteUserById(user.id)
        imageService.deleteUserImage(user.id)
        call.respond(HttpStatusCode.OK)
    }
}