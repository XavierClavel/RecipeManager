package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import common.dto.UserDTO
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.USER_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object UserController: Controller(USER_URL) {
    val userService : UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)

    const val UNAUTHORIZED_TO_EDIT_OTHER_USER = "You are not authorized to edit another user."

    override fun Route.routes() {
        createUser()
        editUser()
        getUser()
        listUsers()
        deleteUser()
    }

    private fun Route.getUser() = get("/{id}") {
        val id = getPathId() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

    private fun Route.listUsers() = get {
        val searchString = call.request.queryParameters["search"]
            ?: return@get call.respond(userService.listUsers())
        call.respond(userService.search(searchString, getPaging()))
    }

    private fun Route.createUser() = post {
        val userDTO = call.receive<UserDTO>()
        val userCreated = userService.createUser(userDTO)
        call.respond(HttpStatusCode.Created, userCreated)
    }

    private fun Route.editUser() = put("/{id}") {
        if (!isActionPerformedOnSelf()) return@put call.respond(HttpStatusCode.Unauthorized, UNAUTHORIZED_TO_EDIT_OTHER_USER)
        val id = getPathId() ?: return@put call.respond(HttpStatusCode.BadRequest)
        val userDTO = call.receive<UserDTO>()
        val response = userService.editUser(id, userDTO) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(response)
    }

    private fun Route.deleteUser() = delete("/{id}") {
        if (!isActionPerformedOnSelf()) return@delete call.respond(HttpStatusCode.Unauthorized, UNAUTHORIZED_TO_EDIT_OTHER_USER)
        val id = getPathId() ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val user = userService.getUser(id) ?: return@delete call.respond(HttpStatusCode.NotFound)
        userService.deleteUserById(user.id)
        imageService.deleteImage(USERS_IMG_PATH, user.id)
        call.respond(HttpStatusCode.OK)
    }

    private fun RoutingContext.isActionPerformedOnSelf(): Boolean =
        getSessionUserId() == getPathId()

}