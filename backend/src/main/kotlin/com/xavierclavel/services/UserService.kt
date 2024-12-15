package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QIngredient
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.infodto.UserInfo
import common.enums.UserRole
import common.infodto.IngredientInfo
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import java.time.Instant

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    //Defined as users who logged in in the last 30 days
    fun countActiveUsers() =
        QUser()
            .where().lastActivityDate.gt(Instant.now().minusSeconds(60 * 60 * 24 * 30).epochSecond)
            .findCount()

    fun findEntityById(userId: Long) : User? =
        QUser().id.eq(userId).findOne()

    fun findByUsername(username: String) : User? =
        QUser().username.eq(username).findOne()

    fun findByMail(mail: String) : User? {
        val mailHash = BCrypt.withDefaults().hashToString(12,mail.toCharArray())
        logger.info {mailHash}
        return QUser().mailHash.eq(mailHash).findOne()
    }

    fun createUser(userDTO: UserDTO): UserInfo =
        User.from(userDTO).insertAndGet().toInfo()

    fun editUser(id: Long, userDTO: UserDTO): UserInfo? =
        QUser().id.eq(id).findOne()?.merge(userDTO)?.updateAndGet()?.toInfo()

    fun registerUserActivity(id: Long) = findEntityById(id)?.registerNewActivity()

    fun deleteUserById(userId: Long) =
        QUser().id.eq(userId).delete()

    fun deleteUserByUsername(username: String) =
        QUser().username.eq(username).delete()

    fun getUserByUsername(username: String) : UserInfo? {
        return findByUsername(username)?.toInfo()
    }

    fun getUser(id: Long) : UserInfo? =
        QUser().id.eq(id).findOne()?.toInfo()

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

    fun search(searchString: String, paging: Paging): List<UserInfo> =
        QUser()
            .username.like("%$searchString%")
            .setPaging(paging)
            .findList()
            .map{it.toInfo()}


}