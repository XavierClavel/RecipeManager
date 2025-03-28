package main.com.xavierclavel.utils

import common.dto.UserDTO
import common.infodto.FollowInfo
import common.infodto.UserInfo
import common.overviewdto.UserOverview
import common.utils.URL.FOLLOW_URL
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


suspend fun HttpClient.follow(id: Long): FollowInfo {
    this.post("$FOLLOW_URL/$id").apply {
        assertEquals(HttpStatusCode.Created, status)
        return Json.decodeFromString<FollowInfo>(bodyAsText())
    }
}

suspend fun HttpClient.unfollow(id: Long) {
    this.delete("$FOLLOW_URL/$id").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.getFollowers(id: Long): Set<FollowInfo> {
    this.get("$FOLLOW_URL/$id/followers").apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<Set<FollowInfo>>(bodyAsText())
    }
}

suspend fun HttpClient.getFollows(id: Long): Set<FollowInfo> {
    this.get("$FOLLOW_URL/$id/follows").apply {
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<Set<FollowInfo>>(bodyAsText())
    }
}
