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
import main.com.xavierclavel.utils.getCookbookRecipes
import main.com.xavierclavel.utils.getCookbookUsers
import main.com.xavierclavel.utils.getLikes
import main.com.xavierclavel.utils.getMe
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
    fun `add multiple users to cookbook`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)

        for (i in 1..3) {
            val user = it.createUser()
            it.addCookbookUser(cookbook.id, user.id, CookbookRole.READER)
        }
        val users = it.getCookbookUsers(cookbook.id)
        assertEquals(3, users.size)
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
    fun `add recipes to cookbook`() = runTestAsAdmin {
        val cookbook = it.createCookbook()
        it.assertCookbookExists(cookbook.id)

        for (i in 1..30) {
            val recipe = it.createRecipe()
            it.addCookbookRecipe(cookbook.id, recipe.id)
        }

        val recipes = it.getCookbookRecipes(cookbook.id)
        assertEquals(20, recipes.size)
    }

}