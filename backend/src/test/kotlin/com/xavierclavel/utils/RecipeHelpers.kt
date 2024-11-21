package main.com.xavierclavel.utils

import common.dto.RecipeDTO
import common.infodto.RecipeInfo
import common.utils.URL.RECIPE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlin.test.assertEquals
import kotlin.test.assertTrue

suspend fun HttpClient.createRecipe(recipe: RecipeDTO) : RecipeInfo {
    this.post(RECIPE_URL){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(recipe)
    }.apply{
        assertEquals(HttpStatusCode.Created, status)
        val response = Json.decodeFromString<RecipeInfo>(bodyAsText())
        assertTrue(response.compareToDTO(recipe))
        return response
    }

}

suspend fun HttpClient.getRecipe(recipeId: Long): RecipeInfo {
    this.get("$RECIPE_URL/$recipeId").apply {
        assertEquals(HttpStatusCode.OK, status)
        val response = Json.decodeFromString<RecipeInfo>(bodyAsText())
        return response
    }
}

suspend fun HttpClient.listRecipes(owner: String?): List<RecipeInfo> {
    this.get(RECIPE_URL) {
        url {
            owner?.let { parameters.append("owner", owner) }
        }
    }.apply {
        assertEquals(HttpStatusCode.OK, status)
        val response = Json.decodeFromString<List<RecipeInfo>>(bodyAsText())
        return response
    }
}

suspend fun HttpClient.countRecipeByUser(username: String): Int {
    this.get("$RECIPE_URL/user/$username/count").apply {
        assertEquals(HttpStatusCode.OK, status)
        return bodyAsText().toInt()
    }
}

suspend fun HttpClient.updateRecipe(recipeId: Long, recipe: RecipeDTO): RecipeInfo {
    this.put("$RECIPE_URL/$recipeId"){
        contentType(ContentType.Application.Json)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        setBody(recipe)
    }.apply{
        assertEquals(HttpStatusCode.OK, status)
        val response = Json.decodeFromString<RecipeInfo>(bodyAsText())
        assertTrue(response.compareToDTO(recipe))
        return response
    }
}

suspend fun HttpClient.deleteRecipe(recipeId: Long) {
    this.delete("$RECIPE_URL/$recipeId").apply{
        assertEquals(HttpStatusCode.OK, status)
    }
    this.assertRecipeDoesNotExist(recipeId)
}

suspend fun HttpClient.assertRecipeExists(recipeId: Long) {
    this.get("$RECIPE_URL/$recipeId").apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.assertRecipeDoesNotExist(recipeId: Long) {
    this.get("$RECIPE_URL/$recipeId").apply {
        assertEquals(HttpStatusCode.NotFound, status)
    }
}