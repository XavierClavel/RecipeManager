package main.com.xavierclavel

import com.xavierclavel.ApplicationTest
import common.dto.UserDTO
import common.utils.URL.USER_URL
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.*
import kotlin.test.assertNotNull

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTest {
        val username = "test_user"
        it.post(USER_URL){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(UserDTO(username))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }

        client.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
            val data = Json.decodeFromString<UserDTO>(bodyAsText())
            assertNotNull(data)
            assertEquals(username, data.username)
        }
    }
}