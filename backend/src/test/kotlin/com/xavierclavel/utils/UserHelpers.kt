package main.com.xavierclavel.utils

import com.xavierclavel.services.UserService
import shared.dto.SearchResult
import shared.dto.UserDTO
import shared.infodto.UserInfo
import shared.utils.URL.AUTH_URL
import shared.utils.URL.USER_URL
import io.ktor.client.HttpClient
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
import java.util.UUID
import kotlin.getValue
import kotlin.test.assertEquals

val userService: UserService by inject(UserService::class.java)

suspend fun HttpClient.createUser(mail: String = UUID.randomUUID().toString()): UserInfo  {
    this.post("$AUTH_URL/signup?locale=EN"){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(UserDTO(mail = mail, username = UUID.randomUUID().toString(), password="password"))
    }.apply {
        assertEquals(HttpStatusCode.Created, status)
        val user = Json.decodeFromString<UserInfo>(bodyAsText())
        userService.validateUser(user.id)
        return user
    }
    //val response = this.getUser(username)
    //assertEquals(username, response.username)
}

suspend fun HttpClient.getUser(id: Long): UserInfo {
    this.get("$USER_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<UserInfo>(bodyAsText())
    }
}

suspend fun HttpClient.getMe(): UserInfo {
    this.get("$AUTH_URL/me").apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<UserInfo>(bodyAsText())
    }
}

suspend fun HttpClient.deleteUser(id: Long) {
    this.delete("$USER_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
    this.assertUserDoesNotExist(id)
}

suspend fun HttpClient.assertUserExists(id: Long) {
    this.get("$USER_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.assertUserDoesNotExist(id: Long) {
    this.get("$USER_URL/$id").apply {
        assertEquals(HttpStatusCode.NotFound, status)
    }
}

suspend fun HttpClient.listUsers() : SearchResult<UserInfo> {
    this.get(USER_URL).apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<SearchResult<UserInfo>>(bodyAsText())
    }
}