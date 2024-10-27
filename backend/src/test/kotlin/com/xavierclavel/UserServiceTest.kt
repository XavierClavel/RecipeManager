package main.com.xavierclavel

import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.logger
import io.ebean.DB
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue
import kotlin.test.Test

class UserServiceTest {
    val userService : UserService by inject(UserService::class.java)

    @Test
    fun test() {
        createUser("test")
        assert(QUser().username.eq("test").findCount() == 1)


        createUser("test2")
        assert(QUser().username.eq("test2").findCount() == 1)
    }

    fun createUser(username: String) {
        val user = User()
        user.username = username
        DB.save(user)
        logger.info { user.id }

    }
}