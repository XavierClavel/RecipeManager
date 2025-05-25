package com.xavierclavel.controllers

import com.xavierclavel.controllers.UserController.mailService
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import com.xavierclavel.plugins.RedisService
import com.xavierclavel.plugins.SessionData
import com.xavierclavel.services.EncryptionService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.getLocale
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.utils.URL.AUTH_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

object AuthController: Controller(AUTH_URL) {
    val userService: UserService by inject(UserService::class.java)
    val redisService: RedisService by inject(RedisService::class.java)
    val encryptionService: EncryptionService by inject(EncryptionService::class.java)
    val redirects = mutableMapOf<String, String>()
    val applicationHttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override fun Route.routes() {
        authenticate("auth-basic") {
            login()
        }
        authenticate("auth-oauth-google") {
            loginGoogleOauth()
            callbackGoogleOauth()
        }
        authenticate("auth-session") {
            whoami()
        }
        logout()
        verifyUser()
        signup()
        requestPasswordReset()
        resetPassword()
    }

    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    private fun Route.login() = post("/login") {
        val username = call.principal<UserIdPrincipal>()?.name.toString()
        val user = userService.getUserByUsername(username) ?: throw NotFoundException(NotFoundCause.USER_NOT_FOUND)
        userService.registerUserActivity(user.id)

        val sessionId = UUID.randomUUID().toString()
        redisService.createSession(sessionId, user)
        call.sessions.set(UserSession(sessionId))
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.loginGoogleOauth() = post("/login-oauth-google") {
    }

    private fun Route.callbackGoogleOauth() = get("/callback-oauth-google") {
        val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
        // redirects home if the url is not found before authorization
        currentPrincipal?.let { principal ->
            principal.state?.let { state ->
                logger.info {"state: $state, accesstoken: ${principal.accessToken}"}
                call.sessions.set(UserSession(principal.accessToken))
                getPersonalGreeting(principal.accessToken)
                redirects[state]?.let { redirect ->
                    call.respondRedirect(redirect)
                    return@get
                }
            }
        }
        call.respondRedirect("/home")
    }

    private suspend fun getPersonalGreeting(
        token: String
    ) {
        val data = applicationHttpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.bodyAsText()
        logger.info {"user data: $data"}
    }

    private fun Route.logout() = post("/logout") {
        call.sessions.clear<UserSession>()
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.verifyUser() = post("/verify") {
        val token = call.queryParameters["token"] ?: throw BadRequestException(BadRequestCause.TOKEN_MISSING)
        logger.info {"'$token'"}
        userService.verifyUser(token)
        call.respond(HttpStatusCode.OK)
    }

    private fun Route.whoami() = get("/me") {
        val userSession = call.sessions.get<UserSession>()
        if (userSession == null) return@get call.respond(HttpStatusCode.Unauthorized, "Session expired")
        val userInfo = userService.getUser(getSessionUserId()) ?: throw NotFoundException(NotFoundCause.USER_NOT_FOUND)
        call.respond(userInfo)
    }

    private fun Route.signup() = post("/signup") {
        val userDTO = call.receive(UserDTO::class)
        logger.info {userDTO}
        if (UserController.userService.existsByUsername(userDTO.username)) throw BadRequestException(BadRequestCause.USERNAME_ALREADY_USED)
        if (UserController.userService.existsByMail(userDTO.mail)) throw BadRequestException(BadRequestCause.MAIL_ALREADY_USED)

        val token = UUID.randomUUID().toString()
        val userCreated = UserController.userService.createUser(userDTO, token)
        mailService.sendVerificationEmail(encryptionService.decrypt(userCreated.mailEncrypted), token, getLocale())
        call.respond(HttpStatusCode.Created, userCreated.toInfo())
    }

    //TODO: send mail to user with verification code to send through another endpoint to chose a new password
    private fun Route.requestPasswordReset() = delete("/password/reset/{mail}") {
        val mail = call.parameters["mail"] ?: throw BadRequestException(BadRequestCause.MAIL_MISSING)
        try {
            val token = userService.requestPasswordReset(mail)
            mailService.sendPasswordResetEmail(mail, token, getLocale())
            call.respond(HttpStatusCode.OK)
        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.OK)
        }
    }

    private fun Route.resetPassword() = put("/password/reset/{token}") {
        val token = call.parameters["token"] ?: throw UnauthorizedException(UnauthorizedCause.INVALID_TOKEN)
        val password = call.queryParameters["password"] ?: throw BadRequestException(BadRequestCause.INVALID_REQUEST)
        userService.resetPassword(token, password)
        call.respond(HttpStatusCode.OK)
    }

    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    suspend fun RoutingContext.getOptionalSessionId(): Long? {
        val session = call.sessions.get<UserSession>() ?: return null
        val userId = redisService.getSessionUserId(session.sessionId)
        if (userId == null) {
            call.sessions.clear<UserSession>()
            throw UnauthorizedException(UnauthorizedCause.SESSION_NOT_FOUND)
        }
        return userId
    }

    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    suspend fun RoutingContext.getSessionUserId(): Long {
        val session = call.sessions.get<UserSession>() ?: throw UnauthorizedException(UnauthorizedCause.SESSION_NOT_FOUND)
        val userId = redisService.getSessionUserId(session.sessionId)
        if (userId == null) {
            call.sessions.clear<UserSession>()
            throw UnauthorizedException(UnauthorizedCause.SESSION_NOT_FOUND)
        }
        return userId
    }

}