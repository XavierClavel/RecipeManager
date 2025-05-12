package com.xavierclavel.controllers

import com.xavierclavel.controllers.AuthController.getSessionUserId
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.MailService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import common.dto.PasswordDTO
import common.dto.UserDTO
import common.dto.UserSettingsDTO
import common.enums.UserRole
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.USER_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

object UserController: Controller(USER_URL) {
    val userService : UserService by inject(UserService::class.java)
    val imageService: ImageService by inject(ImageService::class.java)
    val mailService: MailService by inject(MailService::class.java)

    override fun Route.routes() {
        getUser()
        listUsers()

        authenticate("auth-session") {
            editUser()
            deleteUser()

            updatePassword()

            getSettings()
            updateSettings()
        }

        authenticate("admin-session") {
            setRole()
        }
    }

    private fun Route.getUser() = get("/{id}") {
        val id = getPathId()
        val user = userService.getUser(id)
        call.respond(user)
    }

    private fun Route.listUsers() = get {
            val searchString = call.request.queryParameters["search"]
                ?: return@get call.respond(userService.listUsers())
        call.respond(userService.search(searchString, getPaging()))
    }

    private fun Route.editUser() = put {
        val id = getSessionUserId()
        val userDTO = call.receive<UserDTO>()
        val response = userService.editUser(id, userDTO)
        call.respond(response)
    }

    private fun Route.deleteUser() = delete {
        val id = getSessionUserId()
        val user = userService.getUser(id)
        userService.deleteUserById(user.id)
        imageService.deleteImage(USERS_IMG_PATH, user.id, user.version)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.updatePassword() = put("/password") {
        val id = getSessionUserId()
        val passwordDTO = call.receive<PasswordDTO>()
        if (!userService.isPasswordValid(id, passwordDTO.old)) {
            throw UnauthorizedException(UnauthorizedCause.INVALID_PASSWORD)
        }
        userService.updatePassword(id, passwordDTO.new)
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.getSettings() = get("/settings") {
        call.respond(userService.getSettings(getSessionUserId()))
    }

    private fun Route.updateSettings() = put("/settings") {
        val settingsDTO = call.receive<UserSettingsDTO>()
        userService.updateSettings(getSessionUserId(), settingsDTO)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.setRole() = put("/{id}/role/{role}") {
        val role = UserRole.valueOf(call.parameters["role"]!!)
        val id = getPathId()
        call.respond(userService.setRole(id, role))
    }


}