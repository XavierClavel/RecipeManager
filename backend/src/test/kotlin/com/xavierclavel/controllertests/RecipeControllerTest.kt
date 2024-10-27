package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.UserDTO
import common.utils.URL.USER_URL
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import io.ktor.client.request.delete
import kotlin.test.assertNotNull

class RecipeControllerTest : ApplicationTest() {
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

        it.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
            val data = Json.decodeFromString<UserDTO>(bodyAsText())
            assertNotNull(data)
            assertEquals(username, data.username)
        }
    }

    @Test
    fun `delete user`() = runTest {
        val username = "test_user"
        it.post(USER_URL){
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(UserDTO(username))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }

        it.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
            val data = Json.decodeFromString<UserDTO>(bodyAsText())
            assertNotNull(data)
            assertEquals(username, data.username)
        }

        it.delete("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

        it.get("$USER_URL/$username").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}