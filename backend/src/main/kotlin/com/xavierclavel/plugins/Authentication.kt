package com.xavierclavel.plugins

import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
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
import org.koin.java.KoinJavaComponent
import org.koin.ktor.ext.inject

@OptIn(ExperimentalLettuceCoroutinesApi::class)
fun Application.configureAuthentication() {
    val userService : UserService by inject<UserService>()
    val redisService: RedisService by inject<RedisService>()

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
                if (userService.isPasswordValid(credentials.password, user.passwordHash)) {
                    logger.info {"Login accepted for user ${user.username}"}
                    UserIdPrincipal(credentials.name)
                } else {
                    logger.error {"Login rejected for user ${user.username}"}
                    throw UnauthorizedException(UnauthorizedCause.INVALID_PASSWORD)
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                if (redisService.redis.get("session:${session.sessionId}")?.toLongOrNull() != null) {
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