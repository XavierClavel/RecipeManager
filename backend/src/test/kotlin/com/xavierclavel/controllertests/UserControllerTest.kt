package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import org.junit.Test
import kotlin.test.assertEquals

class UserControllerTest : ApplicationTest() {
    @Test
    fun `create user`() = runTest {
        val username = "test_user"
        it.createUser(username = username)
        it.assertUserExists(username)
    }

    @Test
    fun `delete user`() = runTest {
        val username = "test_user"
        it.createUser(username = username)
        it.assertUserExists(username)
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