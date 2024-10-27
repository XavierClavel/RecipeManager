package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.plugins.*
import common.dto.UserDTO
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
import kotlinx.coroutines.test.TestResult
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ApplicationTest: KoinTest {

    @get:Rule
    val koinTestRule= KoinTestRule()

    @KtorDsl
    public fun runTest(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit): TestResult {
        return testApplication(EmptyCoroutineContext, {
            install(ContentNegotiation) {
                json()
            }
            application {
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

        this.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
            val data = Json.decodeFromString<UserDTO>(bodyAsText())
            assertNotNull(data)
            assertEquals(username, data.username)
        }
    }

    suspend fun HttpClient.deleteUser(username: String) {
        this.delete("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

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