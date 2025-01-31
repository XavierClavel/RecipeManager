package com.xavierclavel.controllers

import com.xavierclavel.controllers.UserController.mailService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.utils.URL.AUTH_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

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
        verifyUser()
        signup()
        resetPassword()
    }

    private fun Route.login() = post("/login") {
        val username = call.principal<UserIdPrincipal>()?.name.toString()
        val user = userService.getUserByUsername(username)!!
        userService.registerUserActivity(user.id)
        call.sessions.set(UserSession(username = username, role = user.role, id = user.id))
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.logout() = post("/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.verifyUser() = post("/verify") {
        val token = call.queryParameters["token"] ?: throw BadRequestException("Token is missing")
        userService.verifyUser(token)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.whoami() = get("/me") {
        val userSession = call.sessions.get<UserSession>()
        if (userSession == null) return@get call.respond(HttpStatusCode.Unauthorized)
        val userInfo = userService.getUserByUsername(userSession.username)!!
        call.respond(userInfo)
    }

    private fun Route.signup() = post("/signup") {
        logger.info {"here"}
        val userDTO = call.receive(UserDTO::class)
        logger.info {userDTO}
        if (UserController.userService.findByUsername(userDTO.username) != null) return@post call.respond(HttpStatusCode.BadRequest, "Username is already used")
        if (UserController.userService.findByMail(userDTO.mail) != null) return@post call.respond(HttpStatusCode.BadRequest, "Mail is already used")

        val token = UUID.randomUUID().toString()
        val userCreated = UserController.userService.createUser(userDTO, token)
        mailService.sendVerificationEmail(userCreated.mail, token)
        call.respond(HttpStatusCode.Created)
    }

    //TODO: send mail to user with verification code to send through another endpoint to chose a new password
    private fun Route.resetPassword() = get("/reset-password/{mail}") {
        val mail = call.parameters["mail"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val user = userService.findByMail(mail)?.toInfo() ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

}