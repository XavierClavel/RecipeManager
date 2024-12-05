package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.enums.CookbookRole
import main.com.xavierclavel.utils.assertCookbookDoesNotExist
import main.com.xavierclavel.utils.assertCookbookExists
import main.com.xavierclavel.utils.assertLikeDoesNotExist
import main.com.xavierclavel.utils.assertLikeExists
import main.com.xavierclavel.utils.createCookbook
import main.com.xavierclavel.utils.createLike
import main.com.xavierclavel.utils.createRecipe
import main.com.xavierclavel.utils.deleteCookbook
import main.com.xavierclavel.utils.deleteLike
import main.com.xavierclavel.utils.getCookbookUsers
import main.com.xavierclavel.utils.getLikes
import main.com.xavierclavel.utils.getMe
import org.junit.Test
import kotlin.test.assertEquals

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

}