package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import io.ktor.utils.io.KtorDsl
import kotlinx.coroutines.test.TestResult
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
    public fun runTest(block: suspend ApplicationTestBuilder.() -> Unit): TestResult {
        return testApplication(EmptyCoroutineContext, {
            install(ContentNegotiation) {
                json()
            }
            application {
                serveRoutes()
            }
            block()
        })
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