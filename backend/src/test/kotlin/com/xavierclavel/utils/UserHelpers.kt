package main.com.xavierclavel.utils

import common.infodto.UserInfo
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

suspend fun HttpClient.createUser(username: String)  {
    this.post(USER_URL){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(UserInfo(username))
    }.apply {
        assertEquals(HttpStatusCode.Created, status)
    }
    val response = this.getUser(username)
    assertEquals(username, response.username)
}

suspend fun HttpClient.getUser(username: String): UserInfo {
    this.get("$USER_URL/$username").apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<UserInfo>(bodyAsText())
    }
}

suspend fun HttpClient.deleteUser(username: String) {
    this.delete("$USER_URL/$username").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
    this.assertUserDoesNotExist(username)
}

suspend fun HttpClient.assertUserExists(username: String) {
    this.get("$USER_URL/$username").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.assertUserDoesNotExist(username: String) {
    this.get("$USER_URL/$username").apply {
        assertEquals(HttpStatusCode.NotFound, status)
    }
}

suspend fun HttpClient.listUsers() : Set<UserInfo> {
    this.get(USER_URL).apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<Set<UserInfo>>(bodyAsText())
    }
}