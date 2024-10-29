package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import org.junit.Test
import kotlin.test.assertTrue

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTestAsAdmin {
        val username = "test_user"
        it.createUser(username = username)
        it.assertUserExists(username)
    }

    @Test
    fun `delete user`() = runTestAsAdmin {
        val username = "test_user"
        it.createUser(username = username)
        it.assertUserExists(username)
        it.deleteUser(username = username)
    }

    @Test
    fun `list users`() = runTestAsAdmin {
        val usernames = setOf("test_user1", "test_user2")
        usernames.forEach { username -> it.createUser(username = username) }
        val response = it.listUsers().map { it.username }.toSet()
        for (username in usernames) {
            assertTrue { response.contains(username) }
        }
    }
}