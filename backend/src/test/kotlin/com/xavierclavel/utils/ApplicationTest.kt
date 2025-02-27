package com.xavierclavel

import com.xavierclavel.plugins.DatabaseManager
import com.xavierclavel.plugins.RedisService
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
import common.dto.UserDTO
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.Plugin
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.*
import io.ktor.utils.io.KtorDsl
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.json.Json
import main.com.xavierclavel.containers.RedisTestContainer
import main.com.xavierclavel.utils.login
import main.com.xavierclavel.utils.logout
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.UUID
import kotlin.coroutines.EmptyCoroutineContext

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ApplicationTest: KoinTest {
    val userService: UserService by inject()

    companion object {
        const val USER1 = "user1"
        const val USER2 = "user2"
        const val password = "Passw0rd"

        @BeforeAll
        @JvmStatic
        fun startKoin() {
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
                single { RedisService(getProperty("redis.url", "redis://redis:6379")) }
            }

            startKoin {
                modules(testModules)
                properties(mapOf("redis.url" to RedisTestContainer.getRedisUri()))
            }
        }

        @AfterAll
        @JvmStatic
        fun stopKoinApplication() {
            stopKoin()
        }
    }



    @BeforeEach
    fun cleanDb() {
        DatabaseManager.getTables().forEach {
            it.findList().forEach { it.delete() }
        }
        val token1 = UUID.randomUUID().toString()
        val token2 = UUID.randomUUID().toString()
        val userDTO1 = UserDTO(username = USER1, password = password, mail = UUID.randomUUID().toString())
        val userDTO2 = UserDTO(username = USER2, password = password, mail = UUID.randomUUID().toString())
        userService.createUser(userDTO1, token1)
        userService.createUser(userDTO2, token2)
        userService.verifyUser(token1)
        userService.verifyUser(token2)
    }


    @KtorDsl
    fun runTestAsAdmin(block: suspend TestBuilderWrapper.() -> Unit) = runTest {
        runAsAdmin {
            this.block()
        }
    }

    @KtorDsl
    fun runTestAsUser(block: suspend TestBuilderWrapper.() -> Unit) = runTest {
        runAsUser1 {
            this.block()
        }
    }

    @KtorDsl
    suspend fun TestBuilderWrapper.runAsAdmin(block: suspend TestBuilderWrapper.() -> Unit) = runAs("admin", password) {
        this.block()
    }

    @KtorDsl
    suspend fun TestBuilderWrapper.runAsUser1(block: suspend TestBuilderWrapper.() -> Unit) = runAs(USER1, password) {
        this.block()
    }

    @KtorDsl
    suspend fun TestBuilderWrapper.runAsUser2(block: suspend TestBuilderWrapper.() -> Unit) = runAs(USER2, password) {
        this.block()
    }


    @KtorDsl
    suspend fun TestBuilderWrapper.runAs(username: String, password: String = "password", block: suspend TestBuilderWrapper.() -> Unit) {
        client.login(username, password)
        this.block()
        client.logout()
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