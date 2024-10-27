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

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTest {
        val username = "test_user"
        it.createUser(username = username)
    }

    @Test
    fun `delete user`() = runTest {
        val username = "test_user"
        it.createUser(username = username)
        it.deleteUser(username = username)
    }

    @Test
    fun `list users`() = runTest {
        val usernames = setOf("test_user1", "test_user2")
        usernames.forEach { username -> it.createUser(username = username) }
        val response = it.listUsers().map { it.username }.toSet()
        assertEquals(usernames, response)
    }
}