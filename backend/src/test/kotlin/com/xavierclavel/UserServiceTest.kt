package main.com.xavierclavel

import com.xavierclavel.ApplicationTest
import com.xavierclavel.config.appModules
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import com.xavierclavel.services.UserService
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.enums.UserRole
import io.ebean.DB
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue
import kotlin.test.Test

class UserServiceTest: ApplicationTest() {
    val userService : UserService by inject(UserService::class.java)

    @Test
    fun test() {
        createUser("test")
        assert(QUser().username.eq("test").findCount() == 1)

        createUser("test2")
        assert(QUser().username.eq("test2").findCount() == 1)
    }

    fun createUser(username: String) {
        userService.createUser(UserDTO(username, UserRole.USER))
        val user = QUser().username.eq(username).findOne()
        logger.info {"id : ${user.id}" }

    }
}