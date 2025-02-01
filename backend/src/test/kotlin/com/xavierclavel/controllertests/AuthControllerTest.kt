package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.exceptions.AuthenticationException
import com.xavierclavel.models.query.QUser
import main.com.xavierclavel.utils.login
import main.com.xavierclavel.utils.signup
import main.com.xavierclavel.utils.verifyUser
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class AuthControllerTest : ApplicationTest() {
    @Test
    fun `signup does not grant access`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        assertThrows<AuthenticationException> { client.login(user.username, password) }
    }

    @Test
    fun `access is granted after email verification`() = runTest {
        val password = UUID.randomUUID().toString()
        val user = client.signup(password = password)
        val token = userService.findEntityById(user.id).verificationToken
        client.verifyUser(token)
        assertDoesNotThrow { client.login(user.username, password) }
    }


}