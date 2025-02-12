package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.exceptions.BadRequestCause
import com.xavierclavel.exceptions.BadRequestException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import main.com.xavierclavel.utils.login
import main.com.xavierclavel.utils.logout
import main.com.xavierclavel.utils.signup
import main.com.xavierclavel.utils.updatePassword
import main.com.xavierclavel.utils.verifyUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.UUID

class AuthControllerTest : ApplicationTest() {
    @Test
    fun `signup does not grant access before email verification`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        assertException<UnauthorizedException>(UnauthorizedCause.USER_NOT_VERIFIED.key) {
            client.login(user.username, password)
        }
    }

    @Test
    fun `access is granted after email verification`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        val token = userService.findEntityById(user.id).token
        client.verifyUser(token)
        assertDoesNotThrow { client.login(user.username, password) }
    }

    @Test
    fun `update user password`() = runTest {
        val oldPassword = UUID.randomUUID().toString()
        val user = client.signup(password = oldPassword)
        val token = userService.findEntityById(user.id).token
        client.verifyUser(token)
        assertDoesNotThrow { client.login(user.username, oldPassword) }

        val newPassword = UUID.randomUUID().toString()
        client.updatePassword(oldPassword, newPassword)
        assertDoesNotThrow { client.login(user.username, newPassword) }
        assertException<UnauthorizedException>(UnauthorizedCause.INVALID_PASSWORD.key) {
            client.login(user.username, oldPassword)
        }
    }

    @Test
    fun `updating password fails if password provided is wrong`() = runTest {
        val oldPassword = UUID.randomUUID().toString()
        val user = client.signup(password = oldPassword)
        val token = userService.findEntityById(user.id).token
        client.verifyUser(token)
        client.login(user.username, oldPassword)

        val wrongPassword = UUID.randomUUID().toString()
        val newPassword = UUID.randomUUID().toString()
        client.login(user.username, oldPassword)


        assertException<UnauthorizedException>(UnauthorizedCause.INVALID_PASSWORD.key) {
            client.updatePassword(wrongPassword, newPassword)
        }

        assertDoesNotThrow {
            client.login(user.username, oldPassword)
            client.logout()
        }

        assertException<UnauthorizedException>(UnauthorizedCause.INVALID_PASSWORD.key) {
            client.login(user.username, newPassword)
        }
    }


}