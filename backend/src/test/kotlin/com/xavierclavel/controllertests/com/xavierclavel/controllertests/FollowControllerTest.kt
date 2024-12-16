package main.com.xavierclavel.controllertests.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.infodto.UserInfo
import common.overviewdto.UserOverview
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.follow
import main.com.xavierclavel.utils.getFollows
import main.com.xavierclavel.utils.unfollow
import org.junit.Test
import kotlin.test.assertEquals

class FollowControllerTest : ApplicationTest() {
    @Test
    fun `follow user`() = runTest {
        var user: UserInfo? = null
        val usersToFollow = mutableListOf<UserInfo>()
        runAsAdmin(it) { client ->
            user = it.createUser("myUser")
            repeat(2) {
                usersToFollow.add(client.createUser())
            }
            val follows1 = it.getFollows(user.id)
            logger.info {"done"}
            assertEquals(setOf<UserOverview>(), follows1.map {it.user}.toSet())
        }
        user!!

        runAs(user.username, "password", it) {
            it.follow(usersToFollow[0].id)
            val follows1 = it.getFollows(user.id)
            assertEquals(setOf(usersToFollow[0].toOverview()), follows1.map {it.user}.toSet())

            it.follow(usersToFollow[1].id)
            val follows2 = it.getFollows(user.id)
            assertEquals(usersToFollow.map { it.toOverview() }.toSet(), follows2.map {it.user}.toSet())
        }
    }

    @Test
    fun `unfollow user`() = runTest {
        var user: UserInfo? = null
        val usersToFollow = mutableListOf<UserInfo>()
        runAsAdmin(it) { client ->
            user = it.createUser("myUser")
            repeat(2) {
                usersToFollow.add(client.createUser())
            }
            val follows1 = it.getFollows(user.id)
            logger.info {"done"}
            assertEquals(setOf<UserOverview>(), follows1.map {it.user}.toSet())
        }
        user!!


        runAs(user.username, "password", it) {
            it.follow(usersToFollow[0].id)
            val follows1 = it.getFollows(user.id)
            assertEquals(setOf(usersToFollow[0].toOverview()), follows1.map {it.user}.toSet())

            it.follow(usersToFollow[1].id)
            val follows2 = it.getFollows(user.id)
            assertEquals(usersToFollow.map { it.toOverview() }.toSet(), follows2.map {it.user}.toSet())

            it.unfollow(usersToFollow[0].id)
            val follows3 = it.getFollows(user.id)
            assertEquals(setOf(usersToFollow[1].toOverview()), follows3.map {it.user}.toSet())
        }
    }
}