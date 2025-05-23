package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import main.com.xavierclavel.utils.assertUserExists
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteUser
import main.com.xavierclavel.utils.listUsers
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTestAsAdmin {
        val username = "test_user"
        val user = client.createUser(username = username)
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
        val usernames = setOf("test_user1", "test_user2")
        usernames.forEach { username -> client.createUser(username = username) }
        val response = client.listUsers().items.map { it.username }.toSet()
        for (username in usernames) {
            assertTrue { response.contains(username) }
        }
    }
}