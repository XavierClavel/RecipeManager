package main.com.xavierclavel.utils

import common.dto.UserDTO
import common.infodto.UserInfo
import common.utils.URL.AUTH_URL
import common.utils.URL.USER_URL
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
import kotlin.test.assertEquals

suspend fun HttpClient.createUser(username: String = "user"): UserInfo  {
    this.post(USER_URL){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(UserDTO(username))
    }.apply {
        assertEquals(HttpStatusCode.Created, status)
        return Json.decodeFromString<UserInfo>(bodyAsText())
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

suspend fun HttpClient.listUsers() : Set<UserInfo> {
    this.get(USER_URL).apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<Set<UserInfo>>(bodyAsText())
    }
}