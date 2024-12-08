package main.com.xavierclavel.utils

import com.xavierclavel.utils.logger
import common.dto.CookbookDTO
import common.enums.CookbookRole
import common.infodto.CookbookInfo
import common.infodto.CookbookRecipeInfo
import common.infodto.CookbookUserInfo
import common.utils.URL.COOKBOOK_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals

val sampleCookbookDto = CookbookDTO(
    title="sample cookbook",
    description="sample cookbook description",
)

suspend fun HttpClient.createCookbook(cookbookDto: CookbookDTO = sampleCookbookDto) : CookbookInfo {
    this.post(COOKBOOK_URL){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(cookbookDto)
    }.apply {
        assertEquals(HttpStatusCode.Created, status)
        return Json.decodeFromString<CookbookInfo>(bodyAsText())
    }
}

suspend fun HttpClient.getCookbook(id: Long): CookbookInfo {
    this.get("$COOKBOOK_URL/$id")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<CookbookInfo>(bodyAsText())
        }
}

suspend fun HttpClient.listCookbooks(user: Long? = null): Set<CookbookInfo> {
    this.get(COOKBOOK_URL) {
        url.apply { if (user != null) parameter("user", user) }
    }
        .apply {
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<Set<CookbookInfo>>(bodyAsText())
        }
}

suspend fun HttpClient.getCookbookUsers(id: Long): Set<CookbookUserInfo> {
    this.get("$COOKBOOK_URL/$id/users")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<Set<CookbookUserInfo>>(bodyAsText())
        }
}

suspend fun HttpClient.getCookbookRecipes(id: Long): Set<CookbookRecipeInfo> {
    this.get("$COOKBOOK_URL/$id/recipes")
        .apply {
            logger.info {this}
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<Set<CookbookRecipeInfo>>(bodyAsText())
        }
}




suspend fun HttpClient.assertCookbookExists(id:Long) {
    this.getCookbook(id)
}

suspend fun HttpClient.assertCookbookDoesNotExist(id:Long) {
    this.get("$COOKBOOK_URL/$id")
        .apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
}

suspend fun HttpClient.deleteCookbook(id: Long) {
    this.delete("$COOKBOOK_URL/$id")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
        }
}

suspend fun HttpClient.addCookbookUser(cookbookId: Long, userId: Long, role: CookbookRole) {
    this.post("$COOKBOOK_URL/$cookbookId/user/$userId?role=$role")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
        }
}

suspend fun HttpClient.deleteCookbookUser(cookbookId: Long, userId: Long) {
    this.delete("$COOKBOOK_URL/$cookbookId/user/$userId")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
        }
}

suspend fun HttpClient.addCookbookRecipe(cookbookId: Long, recipeId: Long) {
    this.post("$COOKBOOK_URL/$cookbookId/recipe/$recipeId")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
        }
}

suspend fun HttpClient.deleteCookbookRecipe(cookbookId: Long, recipeId: Long) {
    this.delete("$COOKBOOK_URL/$cookbookId/recipe/$recipeId")
        .apply {
            assertEquals(HttpStatusCode.OK, status)
        }
}