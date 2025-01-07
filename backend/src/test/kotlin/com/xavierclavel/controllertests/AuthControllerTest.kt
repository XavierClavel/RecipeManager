package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.UserDTO
import common.utils.URL.AUTH_URL
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.junit.Test
import kotlin.test.assertEquals

class AuthControllerTest : ApplicationTest() {
    @Test
    fun `signup`() = runTest {
        val userDTO = UserDTO("test_user", "test_user@example.com", "123456")
        client.post("$AUTH_URL/signup") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(userDTO)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }

    /*
    @Test
    fun `reset password`() = runTestUnauthenticated {
        val mail = "test_user@example.com"
        val username = "test_user_reset"
        val userDTO = UserDTO(username, mail, "123456")
        it.post("$AUTH_URL/signup") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(userDTO)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
        it.get("$AUTH_URL/reset-password/$mail").apply {
            assertEquals(HttpStatusCode.OK, status)
            val response = Json.decodeFromString<UserInfo>(bodyAsText())
            assertEquals(username, response.username)
        }
    }

     */

}