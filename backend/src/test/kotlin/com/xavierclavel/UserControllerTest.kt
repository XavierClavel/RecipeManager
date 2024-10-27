package main.com.xavierclavel

import com.xavierclavel.ApplicationTest
import com.xavierclavel.plugins.configureRouting
import com.xavierclavel.serveRoutes
import common.dto.UserDTO
import common.utils.URL.USER_URL
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class UserControllerTest : ApplicationTest() {
    @Test
    fun testRoot() = runTest {
        client.get(USER_URL).apply {
            assertEquals(HttpStatusCode.OK, status)
            val data = Json.decodeFromString<List<UserDTO>>(bodyAsText())
        }
    }
}