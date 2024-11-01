package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.infodto.UserInfo
import common.enums.UserRole
import org.koin.core.component.KoinComponent

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    fun findById(userId: Long) : User? =
        QUser().id.eq(userId).findOne()

    fun findByUsername(username: String) : User? =
        QUser().username.eq(username).findOne()

    fun findByMail(mail: String) : User? {
        val mailHash = BCrypt.withDefaults().hashToString(12,mail.toCharArray())
        logger.info {mailHash}
        return QUser().mailHash.eq(mailHash).findOne()
    }

    fun createUser(userDTO: UserDTO) {
        User.from(userDTO).insert()
    }

    fun editUser(user: User, userDTO: UserDTO) {
        user.merge(userDTO)
        User.from(userDTO).insert()
    }


    fun deleteUserById(userId: Long) =
        QUser().id.eq(userId).delete()

    fun deleteUserByUsername(username: String) =
        QUser().username.eq(username).delete()

    fun getUserByUsername(username: String) : UserInfo? {
        return findByUsername(username)?.toInfo()
    }

    fun listUsers(): List<UserInfo> =
        QUser().findList().map { it.toInfo() }

    //TODO :parameterize password
    fun setupDefaultAdmin() {
        if (findByUsername("admin") != null) return
        createUser(UserDTO(
            username = "admin",
            password = "Passw0rd",
            role = UserRole.ADMIN,
        ))
    }

    fun isUsernameAvailable(username: String): Boolean =
        findByUsername(username) == null

    fun isMailAvailable(mail: String): Boolean =
        findByMail(mail) == null


}