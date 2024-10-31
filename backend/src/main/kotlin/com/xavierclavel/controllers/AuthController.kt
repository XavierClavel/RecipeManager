package com.xavierclavel.controllers

import com.xavierclavel.models.User
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.utils.URL.AUTH_URL
import common.utils.URL.RECIPE_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.koin.java.KoinJavaComponent.inject

object AuthController: Controller(AUTH_URL) {
    val userService: UserService by inject(UserService::class.java)

    override fun Route.routes() {
        authenticate("auth-basic") {
            login()
        }
        authenticate("auth-session") {
            whoami()
        }
        logout()
        signup()
        resetPassword()
    }

    private fun Route.login() = post("/login") {
        val userName = call.principal<UserIdPrincipal>()?.name.toString()
        call.sessions.set(UserSession(name = userName, count = 1))
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.logout() = post("/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.whoami() = get("/me") {
        val userSession = call.sessions.get<UserSession>()
        if (userSession == null) return@get call.respond(HttpStatusCode.Unauthorized)
        val userInfo = userService.getUserByUsername(userSession.name)!!
        call.respond(userInfo)
    }

    private fun Route.signup() = post("/signup") {
        val userDTO = call.receive(UserDTO::class)
        if (!userService.isUsernameAvailable(userDTO.username)) return@post call.respond(HttpStatusCode.Conflict)
        if (!userService.isMailAvailable(userDTO.mail)) return@post call.respond(HttpStatusCode.BadRequest)
        userService.createUser(userDTO)
        call.respond(HttpStatusCode.Created)
    }

    //TODO: send mail to user with verification code to send through another endpoint to chose a new password
    private fun Route.resetPassword() = get("/reset-password/{mail}") {
        val mail = call.parameters["mail"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.findByMail(mail)?.toInfo() ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

}