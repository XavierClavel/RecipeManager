package main.com.xavierclavel.utils

import com.xavierclavel.services.UserService
import common.dto.UserDTO
import common.infodto.UserInfo
import common.utils.URL.AUTH_URL
import common.utils.URL.USER_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.basicAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.inject
import java.util.UUID
import kotlin.getValue
import kotlin.test.assertEquals

suspend fun HttpClient.signup(username: String = UUID.randomUUID().toString(), password: String = UUID.randomUUID().toString()): UserInfo  {
    this.post("$AUTH_URL/signup"){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(UserDTO(username = username, mail = UUID.randomUUID().toString(), password = password))
    }.apply {
        assertEquals(HttpStatusCode.Created, status)
        val user = Json.decodeFromString<UserInfo>(bodyAsText())
        return user
    }
}

suspend fun HttpClient.login(username: String, password: String) {
    this.post("/v1/auth/login") {
        basicAuth(username = username, password = password)
    }
}

suspend fun HttpClient.logout() {
    this.post("/v1/auth/logout")
}

suspend fun HttpClient.verifyUser(token: String) {
    this.post("/v1/auth/verify") {
        url {
            parameters.append("token", token)
        }
    }.apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}