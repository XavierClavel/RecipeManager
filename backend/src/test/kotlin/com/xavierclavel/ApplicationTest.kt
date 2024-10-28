package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.plugins.configureAuthentication
import com.xavierclavel.services.UserService
import common.dto.RecipeDTO
import common.dto.RecipeInfo
import common.dto.UserDTO
import common.utils.URL.RECIPE_URL
import common.utils.URL.USER_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import io.ktor.utils.io.KtorDsl
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.jupiter.api.TestInstance
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ApplicationTest: KoinTest {
    val userService: UserService by inject()

    @get:Rule
    val koinTestRule= KoinTestRule()

    @KtorDsl
    fun runTest(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) {
        return testApplication(EmptyCoroutineContext, {
            userService.setupDefaultAdmin()
            install(ContentNegotiation) {
                json()
            }
            application {
                configureAuthentication()
                serveRoutes()
            }
            val client = createClient {
                install(HttpCookies) // this is for logging in
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
            client.post("/login") {
                basicAuth(username = "admin", password = "Passw0rd")
            }
            block(client)
        })
    }

    suspend fun HttpClient.createUser(username: String)  {
        this.post(USER_URL){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(UserDTO(username))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
        val response = this.getUser(username)
        assertEquals(username, response.username)
    }

    suspend fun HttpClient.getUser(username: String): UserDTO {
        this.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<UserDTO>(bodyAsText())
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

    suspend fun HttpClient.listUsers() : Set<UserDTO> {
        this.get(USER_URL).apply {
            assertEquals(HttpStatusCode.OK, status)
            return Json.decodeFromString<Set<UserDTO>>(bodyAsText())
        }
    }

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



}

class KoinTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {

                startKoin { modules(appModules) }

                base.evaluate()

                stopKoin()
            }
        }
    }
}