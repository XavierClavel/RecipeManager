package main.com.xavierclavel.utils

import com.xavierclavel.utils.logger
import shared.infodto.LikeInfo
import shared.utils.URL.LIKE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

suspend fun HttpClient.createLike(recipeId: Long)  {
    this.post("$LIKE_URL/$recipeId").apply {
        assertEquals(HttpStatusCode.Created, status)
    }
}


suspend fun HttpClient.deleteLike(recipeId: Long) {
    this.delete("$LIKE_URL/$recipeId").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.getLikes(userId: Long?, recipeId: Long?): Set<LikeInfo> {
    var arguments = listOfNotNull(
        "user=$userId".takeIf { recipeId != null },
        "recipe=$recipeId".takeIf { recipeId != null },)
    this.get("$LIKE_URL${arguments.joinToString("&", "?")}").apply {
        logger.info {status}
        assertEquals(HttpStatusCode.OK, status)
        return Json.decodeFromString<Set<LikeInfo>>(bodyAsText())
    }
}

suspend fun HttpClient.assertLikeDoesNotExist(userId: Long, recipeId: Long) {
    assertEquals(emptySet<LikeInfo>(), this.getLikes(userId, recipeId))
}

suspend fun HttpClient.assertLikeExists(userId: Long, recipeId: Long) {
    assertEquals(1, this.getLikes(userId, recipeId).count())
}
