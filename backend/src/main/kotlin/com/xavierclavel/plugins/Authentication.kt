package com.xavierclavel.plugins

import com.xavierclavel.services.UserService
import com.xavierclavel.utils.UserSession
import com.xavierclavel.utils.logger
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.form
import io.ktor.server.auth.principal
import io.ktor.server.auth.session
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
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
                //if (credentials.name == "jetbrains" && credentials.password == "foobar") {
                if (userService.findByUsername(credentials.name) != null) {
                    logger.info {"login accepted"}
                    UserIdPrincipal(credentials.name)
                } else {
                    logger.error {"login rejected"}
                    null
                }
            }
        }
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                logger.info {credentials}
                if (credentials.name == "jetbrains" && credentials.password == "foobar") {
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
                call.respondRedirect("/login")
            }
        }
    }

    routing {
        authenticate("auth-basic") {
            post("/login") {
                val userName = call.principal<UserIdPrincipal>()?.name.toString()
                call.sessions.set(UserSession(name = userName, count = 1))
                call.respondRedirect("/hello")
            }
        }

        authenticate("auth-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.name}! Visit count is ${userSession?.count}.")
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }


}