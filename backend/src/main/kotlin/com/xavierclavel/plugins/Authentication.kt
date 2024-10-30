package com.xavierclavel.plugins

import at.favre.lib.crypto.bcrypt.BCrypt
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
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val userService : UserService by inject<UserService>()

    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }

    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                logger.info {credentials}
                val hashedPassword = userService.findByUsername(credentials.name)?.passwordHash
                if (hashedPassword != null && BCrypt.verifyer().verify(credentials.password.toCharArray(), hashedPassword).verified) {
                    logger.info {"login accepted"}
                    UserIdPrincipal(credentials.name)
                } else {
                    logger.error {"login rejected"}
                    null
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                logger.info {session}
                if(userService.findByUsername(session.name) != null) {
                    logger.info {"session accepted"}
                    session
                } else {
                    logger.error {"session rejected"}
                    null
                }
            }
            challenge {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

}