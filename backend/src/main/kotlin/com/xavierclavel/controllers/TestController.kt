package com.xavierclavel.controllers

import com.xavierclavel.services.MailService
import com.xavierclavel.utils.Controller
import shared.utils.URL.TEST_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
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