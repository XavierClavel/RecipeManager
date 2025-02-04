package com.xavierclavel.plugins

import com.xavierclavel.exceptions.AuthenticationException
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import org.koin.ktor.ext.inject

@OptIn(ExperimentalLettuceCoroutinesApi::class)
fun Application.configureAuthentication() {
    val userService : UserService by inject<UserService>()

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
                val user = userService.findByUsername(credentials.name) ?: throw AuthenticationException("User not found")
                if (!user.isVerified) throw AuthenticationException("User not verified")
                if (userService.isPasswordValid(credentials.password, user.passwordHash)) {
                    logger.info {"Login accepted for user ${user.username}"}
                    UserIdPrincipal(credentials.name)
                } else {
                    logger.error {"Login rejected for user ${user.username}"}
                    null
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                if (RedisManager.redis.get("session:${session.sessionId}")?.toLongOrNull() != null) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

}