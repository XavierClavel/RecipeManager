package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import main.com.xavierclavel.utils.assertUserExists
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.listUsers
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTestAsAdmin {
        val mail = "test_user@mail.fr"
        val user = client.createUser(mail = mail)
        client.assertUserExists(user.id)
    }

    /*
    @Test
    fun `delete user`() = runTestAsAdmin {
        val username = "test_user"
        val user = it.createUser(username = username)
        it.assertUserExists(user.id)
        it.deleteUser(user.id)
    }

     */

    @Test
    fun `list users`() = runTestAsAdmin {
        val emails = setOf("test_user1@mail.com", "test_user2@mail.com")
        val users = emails.map { mail -> client.createUser(mail = mail) }
        val response = client.listUsers().items.map { it.id }.toSet()
        for (user in users) {
            assertTrue { user.id in response }
        }
    }
}