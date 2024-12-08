package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.enums.CookbookRole
import main.com.xavierclavel.utils.addCookbookRecipe
import main.com.xavierclavel.utils.addCookbookUser
import main.com.xavierclavel.utils.assertCookbookDoesNotExist
import main.com.xavierclavel.utils.assertCookbookExists
import main.com.xavierclavel.utils.assertLikeDoesNotExist
import main.com.xavierclavel.utils.assertLikeExists
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.deleteCookbook
import main.com.xavierclavel.utils.deleteCookbookUser
import main.com.xavierclavel.utils.deleteLike
import main.com.xavierclavel.utils.getCookbook
import main.com.xavierclavel.utils.getCookbookRecipes
import main.com.xavierclavel.utils.getCookbookUsers
import main.com.xavierclavel.utils.getLikes
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.listCookbooks
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CookbookControllerTest : ApplicationTest() {
    @Test
    fun `create cookbook`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)
    }

    @Test
    fun `creating cookbook grants admin access to it`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)
        val users = it.getCookbookUsers(cookbook.id)
        assertEquals(1, users.size)
        assertEquals(CookbookRole.OWNER, users.first().role)
    }

    @Test
    fun `delete cookbook`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)
        it.deleteCookbook(cookbook.id)
        it.assertCookbookDoesNotExist(cookbook.id)
    }

    @Test
    fun `add user to cookbook`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)
        val user = userService.getUserByUsername("user1")!!
        it.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
        val users = it.getCookbookUsers(cookbook.id)
        assertEquals(2, users.size)
        assertTrue(users.any { it.username == "user1" })
    }

    @Test
    fun `add multiple users to cookbook`() = runTestAsAdmin { client ->
        val cookbook = client.createCookbook()
        client.assertCookbookExists(cookbook.id)

        repeat(3) {
            val user = client.createUser()
            client.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
        }
        val users = client.getCookbookUsers(cookbook.id)
        assertEquals(4, users.size)
    }

    @Test
    fun `delete cookbook user`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)

        val user = userService.getUserByUsername("user1")!!
        it.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
        val users = it.getCookbookUsers(cookbook.id)
        assertEquals(2, users.size)
        assertTrue(users.any { it.username == "user1" })

        it.deleteCookbookUser(cookbook.id, user.id)
        val users2 = it.getCookbookUsers(cookbook.id)
        assertEquals(1, users2.size)
        assertTrue(users2.none { it.username == "user1" })
    }

    @Test
    fun `add recipes to cookbook`() = runTestAsAdmin { client ->
        val cookbook = client.createCookbook()
        client.assertCookbookExists(cookbook.id)

        repeat(30) {
            val recipe = client.createRecipe()
            client.addCookbookRecipe(cookbook.id, recipe.id)
        }

        val recipes = client.getCookbookRecipes(cookbook.id)
        assertEquals(20, recipes.size)
    }

    @Test
    fun `list user cookbooks`() = runTestAsAdmin {
        val cookbook1 = it.createCookbook()
        val cookbook2 = it.createCookbook()
        val cookbook3 = it.createCookbook()
        val cookbook4 = it.createCookbook()

        val user1 = it.createUser()
        val user2 = it.createUser()
        val user3 = it.createUser()

        it.addCookbookUser(cookbook1.id, user1.id, CookbookRole.READER)
        it.addCookbookUser(cookbook2.id, user1.id, CookbookRole.READER)
        it.addCookbookUser(cookbook2.id, user2.id, CookbookRole.READER)
        it.addCookbookUser(cookbook3.id, user2.id, CookbookRole.READER)

        val result1 = it.listCookbooks(user1.id).map { it.id }.toSet()
        val result2 = it.listCookbooks(user2.id).map { it.id }.toSet()
        val result3 = it.listCookbooks(user3.id).map { it.id }.toSet()

        val expected1 = setOf(cookbook1.id, cookbook2.id)
        val expected2 = setOf(cookbook2.id, cookbook3.id)
        val expected3 = setOf<Long>()

        assertEquals(expected1, result1)
        assertEquals(expected2, result2)
        assertEquals(expected3, result3)
    }

    @Test
    fun `count cookbook users and recipes`() = runTestAsAdmin { client ->
        val cookbook = client.createCookbook()
        client.assertCookbookExists(cookbook.id)

        repeat(2) {
            val user = client.createUser()
            client.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
        }

        repeat(2) {
            val recipe = client.createRecipe()
            client.addCookbookRecipe(cookbook.id, recipe.id)
        }

        val result = client.getCookbook(cookbook.id)
        assertEquals(3, result.usersCount)
        assertEquals(2, result.recipesCount)
    }

}