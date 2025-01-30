package com.xavierclavel.controllers

import com.xavierclavel.services.ImageService
import com.xavierclavel.services.MailService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.Controller
import com.xavierclavel.utils.getPaging
import com.xavierclavel.utils.getPathId
import com.xavierclavel.utils.getSessionUserId
import common.dto.UserDTO
import common.utils.Filepath.USERS_IMG_PATH
import common.utils.URL.TEST_URL
import common.utils.URL.USER_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.java.KoinJavaComponent.inject

object TestController: Controller(TEST_URL) {
    val mailService : MailService by inject(MailService::class.java)

    override fun Route.routes() {
        sendMail()
    }

    private fun Route.sendMail() = post("/mail/{address}") {
        val mail = call.parameters["address"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        mailService.sendEmail(mail,"Test", "Hello world")
        call.respond(HttpStatusCode.OK)
    }

}