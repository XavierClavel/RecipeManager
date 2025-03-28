package main.com.xavierclavel.controllertests

import com.xavierclavel.ApplicationTest
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.infodto.UserInfo
import common.overviewdto.UserOverview
import main.com.xavierclavel.utils.createUser
import main.com.xavierclavel.utils.follow
import main.com.xavierclavel.utils.getFollowers
import main.com.xavierclavel.utils.getFollows
import main.com.xavierclavel.utils.getMe
import main.com.xavierclavel.utils.getUser
import main.com.xavierclavel.utils.unfollow
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class FollowControllerTest : ApplicationTest() {
    @Test
    fun `follow user`() = runTest {
        var user: UserInfo? = null
        val usersToFollow = mutableListOf<UserInfo>()
        runAsAdmin {
            user = client.createUser("myUser")
            repeat(2) {
                usersToFollow.add(client.createUser())
            }
            val follows1 = client.getFollows(user.id)
            logger.info {"done"}
            assertEquals(setOf<UserOverview>(), follows1.map {it.user}.toSet())
        }
        user!!

        runAs(user.username, "password") {
            client.follow(usersToFollow[0].id)
            val follows1 = client.getFollows(user.id)
            assertEquals(setOf(usersToFollow[0].toOverview()), follows1.map {it.user}.toSet())

            client.follow(usersToFollow[1].id)
            val follows2 = client.getFollows(user.id)
            assertEquals(usersToFollow.map { it.toOverview() }.toSet(), follows2.map {it.user}.toSet())
        }
    }

    @Test
    fun `unfollow user`() = runTest {
        var user: UserInfo? = null
        val usersToFollow = mutableListOf<UserInfo>()
        runAsAdmin {
            user = client.createUser("myUser")
            repeat(2) {
                usersToFollow.add(client.createUser())
            }
            val follows1 = client.getFollows(user.id)
            logger.info {"done"}
            assertEquals(setOf<UserOverview>(), follows1.map {it.user}.toSet())
        }
        user!!


        runAs(user.username, "password") {
            client.follow(usersToFollow[0].id)
            val follows1 = client.getFollows(user.id)
            assertEquals(setOf(usersToFollow[0].toOverview()), follows1.map {it.user}.toSet())

            client.follow(usersToFollow[1].id)
            val follows2 = client.getFollows(user.id)
            assertEquals(usersToFollow.map { it.toOverview() }.toSet(), follows2.map {it.user}.toSet())

            client.unfollow(usersToFollow[0].id)
            val follows3 = client.getFollows(user.id)
            assertEquals(setOf(usersToFollow[1].toOverview()), follows3.map {it.user}.toSet())
        }
    }

    @Test
    fun `get followers`() = runTest {
        var user: UserInfo? = null
        var follower1: UserInfo? = null
        var follower2: UserInfo? = null
        runAsAdmin {
            user = client.createUser("myUser")
            follower1 = client.createUser("follower1")
            follower2 = client.createUser("follower2")

            val follows1 = client.getFollows(user.id)
            assertEquals(setOf<UserOverview>(), follows1.map {it.user}.toSet())
        }
        user!!
        follower1!!
        follower2!!


        runAs(follower1.username, "password") {
            client.follow(user.id)
            val followers = client.getFollowers(user.id)
            assertEquals(setOf(follower1.toOverview()), followers.map {it.user}.toSet())
        }

        runAs(follower2.username, "password") {
            client.follow(user.id)
            val followers = client.getFollowers(user.id)
            assertEquals(setOf(follower1.toOverview(), follower2.toOverview()), followers.map {it.user}.toSet())
        }

        runAs(follower1.username, "password") {
            client.unfollow(user.id)
            val followers = client.getFollowers(user.id)
            assertEquals(setOf(follower2.toOverview()), followers.map {it.user}.toSet())
        }

    }

    @Test
    fun `count followers`() = runTest {
        var user: Long = setupTestUser("user3")
        var follower1: UserInfo? = null
        var follower2: UserInfo? = null
        runAsAdmin {
            val result = client.getUser(user)
            follower1 = userService.getUserByUsername("user1")!!
            follower2 = userService.getUserByUsername("user2")!!

            assertEquals(0, result.followersCount)
        }
        user!!
        follower1!!
        follower2!!


        runAs(follower1.username) {
            client.follow(user)
            val userInfo = client.getUser(user)
            logger.info { userInfo }
            assertEquals(1, userInfo.followersCount)
        }

        runAs(follower2.username) {
            client.follow(user)
            val userInfo = client.getUser(user)
            assertEquals(2, userInfo.followersCount)
        }

        runAs(follower1.username) {
            client.unfollow(user)
            val userInfo = client.getUser(user)
            assertEquals(1, userInfo.followersCount)
        }
    }
}