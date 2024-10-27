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
    fun `create user`() {
        createUser("test")
        createUser("test2")
    }

    @Test
    fun `delete user`() {
        createUser("test")
        deleteUser("test")
    }

    fun createUser(username: String) {
        userService.createUser(UserDTO(username, UserRole.USER))
        assert(QUser().username.eq(username).findCount() == 1)
    }

    fun deleteUser(username: String) {
        userService.deleteUserByUsername(username)
        assert(!QUser().username.eq(username).exists())
    }
}