package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import main.com.xavierclavel.utils.assertException
import main.com.xavierclavel.utils.login
import main.com.xavierclavel.utils.logout
import main.com.xavierclavel.utils.requestPasswordReset
import main.com.xavierclavel.utils.resetPassword
import main.com.xavierclavel.utils.signup
import main.com.xavierclavel.utils.updatePassword
import main.com.xavierclavel.utils.verifyUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID
import kotlin.test.assertEquals

class AuthControllerTest : ApplicationTest() {
    @Test
    fun `signup does not grant access before email verification`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        client.login(user.username, password).apply {
            assertEquals(status, HttpStatusCode.Unauthorized)
            assertEquals(bodyAsText(), UnauthorizedCause.USER_NOT_VERIFIED.key)
        }
    }

    @Test
    fun `access is granted after email verification`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        val token = userService.getEntityById(user.id).token
        client.verifyUser(token)
        assertDoesNotThrow { client.login(user.username, password) }
    }

    @Test
    fun `update user password`() = runTest {
        val oldPassword = UUID.randomUUID().toString()
        val user = client.signup(password = oldPassword)
        val token = userService.getEntityById(user.id).token
        client.verifyUser(token)
        assertDoesNotThrow { client.login(user.username, oldPassword) }

        val newPassword = UUID.randomUUID().toString()
        client.updatePassword(oldPassword, newPassword)
        client.login(user.username, newPassword).apply {
            assertEquals(status, HttpStatusCode.OK)
        }
        client.login(user.username, oldPassword).apply {
            assertEquals(status, HttpStatusCode.Unauthorized)
            assertEquals(bodyAsText(), UnauthorizedCause.INVALID_PASSWORD.key)
        }
    }

    @Test
    fun `updating password fails if password provided is wrong`() = runTest {
        val oldPassword = UUID.randomUUID().toString()
        val user = client.signup(password = oldPassword)
        val token = userService.getEntityById(user.id).token
        client.verifyUser(token)
        client.login(user.username, oldPassword)

        val wrongPassword = UUID.randomUUID().toString()
        val newPassword = UUID.randomUUID().toString()
        client.login(user.username, oldPassword)

        client.updatePassword(wrongPassword, newPassword).apply {
            assertEquals(status, HttpStatusCode.Unauthorized)
            assertEquals(bodyAsText(), UnauthorizedCause.INVALID_PASSWORD.key)
        }

        client.login(user.username, oldPassword).apply {
            assertEquals(status, HttpStatusCode.OK)
        }
        client.logout().apply {
            assertEquals(status, HttpStatusCode.OK)
        }

        client.login(user.username, newPassword).apply {
            assertEquals(status, HttpStatusCode.Unauthorized)
            assertEquals(bodyAsText(), UnauthorizedCause.INVALID_PASSWORD.key)
        }
    }

    @Test
    fun `reset user password`() = runTest {
        val oldPassword = UUID.randomUUID().toString()
        val newPassword = UUID.randomUUID().toString()
        val user = client.signup(password = oldPassword)
        val token = userService.getEntityById(user.id).token
        client.verifyUser(token)
        println("login1")
        client.login(user.username, oldPassword)
        client.logout()

        val mail = userService.getEntityById(user.id).mail
        client.requestPasswordReset(mail)
        val newToken = userService.getEntityById(user.id).token
        client.resetPassword(newToken, newPassword)
        println("login2")
        client.login(user.username, newPassword).apply {
            assertEquals(HttpStatusCode.OK, status)
        }
        client.logout()

        println("login3")
        client.login(user.username, oldPassword).apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
            assertEquals(UnauthorizedCause.INVALID_PASSWORD.key, bodyAsText())
        }
    }


}