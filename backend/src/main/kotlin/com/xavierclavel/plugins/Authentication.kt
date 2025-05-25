package com.xavierclavel.plugins

import com.xavierclavel.controllers.AuthController
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Configuration
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.auth.oauth
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.client.engine.cio.*
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.koin.java.KoinJavaComponent
import org.koin.ktor.ext.inject

@OptIn(ExperimentalLettuceCoroutinesApi::class)
fun Application.configureAuthentication() {
    val userService : UserService by inject<UserService>()
    val redisService: RedisService by inject<RedisService>()
    val configuration: Configuration by inject<Configuration>()



    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.httpOnly = true
            cookie.path = "/"
            cookie.maxAgeInSeconds = 7 * 24 * 60 * 60
        }
    }

    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                val user = userService.findByUsername(credentials.name) ?: throw NotFoundException(NotFoundCause.USER_NOT_FOUND)
                if (!user.isVerified) throw UnauthorizedException(UnauthorizedCause.USER_NOT_VERIFIED)
                if (userService.isPasswordValid(credentials.password, user.passwordHash!!)) {
                    logger.info {"Login accepted for user ${user.username}"}
                    UserIdPrincipal(credentials.name)
                } else {
                    logger.error {"Login rejected for user ${user.username}"}
                    throw UnauthorizedException(UnauthorizedCause.INVALID_PASSWORD)
                }
            }
        }

        oauth("auth-oauth-google") {
            // Configure oauth authentication
            urlProvider = { "${configuration.backend.url}/auth/callback-oauth-google" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://oauth2.googleapis.com/token",
                    requestMethod = HttpMethod.Post,
                    clientId = configuration.oauth.google.clientId,
                    clientSecret = configuration.oauth.google.clientSecret,
                    defaultScopes = listOf("openid", "profile", "email"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            AuthController.redirects[state] = it
                        }
                    }
                )
            }
            client = AuthController.applicationHttpClient
        }

        session<UserSession>("auth-session") {
            validate { session ->
                if (redisService.hasSession(session.sessionId)) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, UnauthorizedCause.SESSION_NOT_FOUND.key)
            }
        }

        session<UserSession>("admin-session") {
            validate { session ->
                if (redisService.isUserAdmin(session.sessionId)) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized, UnauthorizedCause.SESSION_NOT_FOUND.key)
            }
        }
    }

}