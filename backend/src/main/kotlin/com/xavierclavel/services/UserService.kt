package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.exceptions.AuthenticationException
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
import common.overviewdto.UserOverview
import io.ebean.Paging
import io.ktor.server.plugins.NotFoundException
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

    fun findEntityById(userId: Long) : User =
        QUser().id.eq(userId).findOne() ?: throw NotFoundException("User not found")

    fun findByUsername(username: String) : User? =
        QUser().username.eq(username).findOne()

    fun findByMail(mail: String) : User {
        return QUser().mail.eq(mail).findOne() ?: throw NotFoundException("Mail not found")
    }

    fun findByToken(token: String) : User =
        QUser().verificationToken.eq(token).findOne() ?: throw NotFoundException("Invalid verification token")

    fun existsById(id: Long) = QUser().id.eq(id).exists()
    fun existsByMail(mail: String) = QUser().mail.eq(mail).exists()
    fun existsByUsername(username: String) = QUser().username.eq(username).exists()

    fun createUser(userDTO: UserDTO, token: String = ""): User =
        User.from(userDTO, token).insertAndGet()

    fun editUser(id: Long, userDTO: UserDTO): UserInfo? =
        QUser().id.eq(id).findOne()?.merge(userDTO)?.updateAndGet()?.toInfo()

    fun registerUserActivity(id: Long) = findEntityById(id).registerNewActivity()

    fun deleteUserById(userId: Long) =
        QUser().id.eq(userId).delete()

    fun deleteUserByUsername(username: String) =
        QUser().username.eq(username).delete()

    fun getUserByUsername(username: String) : UserInfo? {
        return findByUsername(username)?.toInfo()
    }

    fun getUser(id: Long) : UserInfo? =
        QUser().id.eq(id).findOne()?.toInfo()

    fun listUsers(): List<UserOverview> =
        QUser().findList().map { it.toOverview() }

    //TODO :parameterize password
    fun setupDefaultAdmin() {
        if (findByUsername("admin") != null) return
        val user = createUser(UserDTO(
            username = "admin",
            password = "Passw0rd",
            role = UserRole.ADMIN,
        ))
        validateUser(user.id)
    }

    fun verifyUser(token:String): UserInfo {
        val user = findByToken(token)
        return user.validate().updateAndGet().toInfo()
    }

    fun isPasswordValid(id: Long, password: String): Boolean =
        isPasswordValid(password, findEntityById(id).passwordHash)

    fun isPasswordValid(password: String, hash: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hash).verified

    fun updatePassword(id: Long, password: String) =
        findEntityById(id).updatePassword(password).updateAndGet().toInfo()

    fun resetPassword(token: String, password: String) =
        findByToken(token).updatePassword(password).updateAndGet().toInfo()

    fun updateToken(mail: String, token: String) =
        findByMail(mail).updateToken(token).updateAndGet().toInfo()

    fun validateUser(id: Long) =
        findEntityById(id).validate().updateAndGet()

    fun search(searchString: String, paging: Paging): List<UserInfo> =
        QUser()
            .username.like("%$searchString%")
            .setPaging(paging)
            .findList()
            .map{it.toInfo()}


}