package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.exceptions.ForbiddenException
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.MailService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import common.dto.PasswordDTO
import common.dto.UserDTO
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.USER_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

object UserController: Controller(USER_URL) {
    val userService : UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)
    val mailService: MailService by inject(MailService::class.java)

    override fun Route.routes() {
        editUser()
        getUser()
        listUsers()
        deleteUser()

        updatePassword()
        deletePassword()
        resetPassword()
    }

    private fun Route.getUser() = get("/{id}") {
        val id = getPathId()
        val user = userService.getUser(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

    private fun Route.listUsers() = get {
        val searchString = call.request.queryParameters["search"]
            ?: return@get call.respond(userService.listUsers())
        call.respond(userService.search(searchString, getPaging()))
    }

    private fun Route.editUser() = put("/{id}") {
        checkIfActionPerformedOnSelf()
        val id = getPathId()
        val userDTO = call.receive<UserDTO>()
        val response = userService.editUser(id, userDTO) ?: return@put call.respond(HttpStatusCode.NotFound)
        call.respond(response)
    }

    private fun Route.deleteUser() = delete("/{id}") {
        checkIfActionPerformedOnSelf()
        val id = getPathId()
        val user = userService.getUser(id) ?: return@delete call.respond(HttpStatusCode.NotFound)
        userService.deleteUserById(user.id)
        imageService.deleteImage(USERS_IMG_PATH, user.id)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.updatePassword() = put("/password/{id}") {
        checkIfActionPerformedOnSelf()
        val id = getPathId()
        val passwordDTO = call.receive<PasswordDTO>()
        if (!userService.isPasswordValid(id, passwordDTO.old)) {
            throw BadRequestException("Invalid password")
        }
        userService.updatePassword(id, passwordDTO.new)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.deletePassword() = delete("/password/{mail}") {
        val mail = call.parameters["mail"] ?: throw BadRequestException("Mail parameter missing")
        val uuid = UUID.randomUUID().toString()
        userService.updateToken(mail, uuid)
        mailService.sendPasswordResetEmail(mail, uuid)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.resetPassword() = post("/password") {
        val passwordDTO = call.receive<PasswordDTO>()
        userService.resetPassword(passwordDTO.old, passwordDTO.new)
        call.respond(HttpStatusCode.OK)
    }

    private suspend fun RoutingContext.checkIfActionPerformedOnSelf() {
        if(getSessionUserId() != getPathId()) throw ForbiddenException("You are not authorized to edit another user.")
    }

}