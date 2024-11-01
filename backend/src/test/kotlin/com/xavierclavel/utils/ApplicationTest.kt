package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.plugins.configureAuthentication
import com.xavierclavel.services.UserService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.*
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ApplicationTest: KoinTest {
    val userService: UserService by inject()

    @get:Rule
    val koinTestRule= KoinTestRule()

    @KtorDsl
    fun runTestAsAdmin(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) {
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
            client.post("/v1/auth/login") {
                basicAuth(username = "admin", password = "Passw0rd")
            }
            block(client)
            client.post("/v1/auth/logout")
        })
    }

    @KtorDsl
    fun runTestUnauthenticated(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) {
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
            block(client)
        })
    }

}

class KoinTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {

                startKoin { modules(appModules) }

                try {
                    base.evaluate()
                } finally {
                    stopKoin()
                }
            }
        }
    }
}