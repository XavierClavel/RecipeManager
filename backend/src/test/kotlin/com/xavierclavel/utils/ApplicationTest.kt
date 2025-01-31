package com.xavierclavel

import com.xavierclavel.config.appModules
import com.xavierclavel.plugins.DatabaseManager
import com.xavierclavel.plugins.configureAuthentication
import com.xavierclavel.services.CookbookService
import com.xavierclavel.services.CustomIngredientService
import com.xavierclavel.services.DashboardService
import com.xavierclavel.services.ExportService
import com.xavierclavel.services.FollowService
import com.xavierclavel.services.ImageService
import com.xavierclavel.services.IngredientService
import com.xavierclavel.services.LikeService
import com.xavierclavel.services.MailService
import com.xavierclavel.services.RecipeIngredientService
import com.xavierclavel.services.RecipeService
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.Plugin
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import io.ktor.utils.io.KtorDsl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.coroutines.EmptyCoroutineContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ApplicationTest: KoinTest {
    val userService: UserService by inject()

    @get:Rule
    val koinTestRule = KoinTestRule()

    @get:Rule
    val cleanDbRule = CleanDbRule()



    @KtorDsl
    fun runTestAsAdmin(block: suspend TestBuilderWrapper.() -> Unit) {
        runTest {
            runAs("admin", "Passw0rd") {
                block()
            }
        }
    }

    @KtorDsl
    suspend fun TestBuilderWrapper.runAsAdmin(block: suspend TestBuilderWrapper.() -> Unit) {
        runAs("admin", "Passw0rd") {
            this.block()
        }
    }


    @KtorDsl
    suspend fun TestBuilderWrapper.runAs(username: String, password: String = "password", block: suspend TestBuilderWrapper.() -> Unit) {
        client.post("/v1/auth/login") {
            basicAuth(username = username, password = password)
        }
        this.block()
        client.post("/v1/auth/logout")
    }

    @KtorDsl
    fun runTest(block: suspend TestBuilderWrapper.() -> Unit) {
        return testApplication(EmptyCoroutineContext) {
            userService.setupDefaultAdmin()
            install(ContentNegotiation) {
                json()
            }
            application {
                configureAuthentication()
                serveRoutes()
            }

            val wrapper = TestBuilderWrapper(this)
            wrapper.block() // Use the wrapper in the block
        }
    }


}

class KoinTestRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {
                val mockMailService = mockk<MailService>()
                every {mockMailService.sendVerificationEmail(any(),any())} answers {}
                every {mockMailService.sendPasswordResetEmail(any(),any())} answers {}

                val testModules = module {
                    single { RecipeService() }
                    single { UserService() }
                    single { IngredientService() }
                    single { ImageService() }
                    single { ExportService() }
                    single { LikeService() }
                    single { CookbookService() }
                    single { DashboardService() }
                    single { RecipeIngredientService() }
                    single { CustomIngredientService() }
                    single { FollowService() }
                    single { mockMailService }
                }

                startKoin { modules(testModules) }

                try {
                    base.evaluate()
                } finally {
                    stopKoin()
                }
            }
        }
    }
}

class CleanDbRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {

            override fun evaluate() {
                DatabaseManager.getTables().forEach {
                    it.findList().forEach { it.delete() }
                }
                base.evaluate()
            }
        }
    }
}

class TestBuilderWrapper(private val builder: ApplicationTestBuilder) {
    val client: HttpClient by lazy {
        builder.client.config {
            install(HttpCookies)
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }

    // Delegate install with proper types
    fun <P : Any, B : Any, F : Any> install(plugin: Plugin<Application, B, F>, configure: B.() -> Unit = {}) {
        builder.install(plugin, configure)
    }

    fun application(block: Application.() -> Unit) = builder.application(block)
}