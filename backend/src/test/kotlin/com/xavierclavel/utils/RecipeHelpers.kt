package main.com.xavierclavel.utils

import com.xavierclavel.utils.logger
import common.dto.RecipeDTO
import common.enums.DishClass
import common.enums.Sort
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

val recipeDTO = RecipeDTO(
    title = "My recipe",
    description = "My description",
    steps = mutableListOf(
        "cut",
        "cook"
    )
)

suspend fun HttpClient.createRecipe(recipe: RecipeDTO = recipeDTO) : RecipeInfo {
    logger.info { "create" }
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

suspend fun HttpClient.getRecipeRaw(recipeId: Long) =
    this.get("$RECIPE_URL/$recipeId")

suspend fun HttpClient.getRecipe(recipeId: Long): RecipeInfo {
    this.getRecipeRaw(recipeId).apply {
        assertEquals(HttpStatusCode.OK, status)
        val response = Json.decodeFromString<RecipeInfo>(bodyAsText())
        return response
    }
}

suspend fun HttpClient.listRecipes(
    user: Long? = null,
    sort: Sort? = null,
    cookbook: Long? = null,
    likedBy: Long? = null,
    followedBy: Long? = null,
    cookbookUser: Long? = null,
    search: String? = null,
    ingredient: Set<Long> = setOf(),
    dishClasses: Set<DishClass> = setOf(),
): List<RecipeInfo> {
    this.get(RECIPE_URL) {
        url {
            user?.let { parameters.append("user", it.toString()) }
            cookbook?.let {parameters.append("cookbook", it.toString())}
            likedBy?.let { parameters.append("likedBy", it.toString()) }
            followedBy?.let { parameters.append("followedBy", it.toString()) }
            sort?.let { parameters.append("sort", it.toString()) }
            cookbookUser?.let { parameters.append("cookbookUser", it.toString()) }
            search?.let { parameters.append("search", it.toString()) }
            ingredient.takeIf { it.isNotEmpty() }?.let { parameters.append("ingredient", it.joinToString(","))}
            dishClasses.takeIf { it.isNotEmpty() }?.let { parameters.append("dishClasses", it.joinToString(",")) }
        }
    }.apply {
        assertEquals(HttpStatusCode.OK, status)
        val response = Json.decodeFromString<List<RecipeInfo>>(bodyAsText())
        return response
    }
}

suspend fun HttpClient.updateRecipe(recipeId: Long, recipe: RecipeDTO): RecipeInfo {
    logger.info {"update"}
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
    this.getRecipeRaw(recipeId).apply {
        assertEquals(HttpStatusCode.OK, status)
    }
}

suspend fun HttpClient.assertRecipeDoesNotExist(recipeId: Long) {
    this.getRecipeRaw(recipeId).apply {
        assertEquals(HttpStatusCode.NotFound, status)
    }
}