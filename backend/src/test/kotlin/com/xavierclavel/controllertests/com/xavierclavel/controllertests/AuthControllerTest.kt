package main.com.xavierclavel.controllertests.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import common.dto.UserDTO
import common.dto.UserInfo
import common.utils.URL.AUTH_URL
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
import kotlin.test.assertTrue

class AuthControllerTest : ApplicationTest() {
    @Test
    fun `signup`() = runTestUnauthenticated {
        val userDTO = UserDTO("test_user", "test_user@example.com", "123456")
        it.post("$AUTH_URL/signup") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(userDTO)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }

    @Test
    fun `reset password`() = runTestUnauthenticated {
        val mail = "test_user@example.com"
        val username = "test_user"
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

}