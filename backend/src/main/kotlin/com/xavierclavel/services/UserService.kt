package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.exceptions.UnauthorizedCause
import com.xavierclavel.exceptions.UnauthorizedException
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.dto.UserSettingsDTO
import common.infodto.UserInfo
import common.enums.UserRole
import common.overviewdto.UserOverview
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    //Defined as users who logged in in the last 30 days
    fun countActiveUsers() =
        QUser()
            .where().lastActivityDate.gt(LocalDateTime.now(ZoneOffset.UTC).minusMonths(1))
            .findCount()

    fun getEntityById(userId: Long) : User =
        QUser().id.eq(userId).findOne() ?: throw NotFoundException(NotFoundCause.USER_NOT_FOUND)

    fun findByUsername(username: String) : User? =
        QUser().username.eq(username).findOne()

    fun findByMail(mail: String) : User {
        return QUser().mail.eq(mail).findOne() ?: throw NotFoundException(NotFoundCause.MAIL_NOT_FOUND)
    }


    fun listAllTokens() {
        QUser().findList().forEach {logger.info {"'${it.token}'"}}
    }

    fun requestPasswordReset(mail: String): String {
        val user = findByMail(mail)
        val token = generateToken()
        user.updateToken(token)
        return token
    }

    fun generateToken(): String {
        var token = UUID.randomUUID().toString()
        while (isTokenUsed(token)) {
            token = UUID.randomUUID().toString()
        }
        return token
    }

    fun isTokenUsed(token: String): Boolean =
        QUser().token.eq(token).exists()

    fun findByToken(token: String) : User =
        QUser().token.eq(token).findOne() ?: throw UnauthorizedException(UnauthorizedCause.INVALID_TOKEN)


    fun existsById(id: Long) = QUser().id.eq(id).exists()
    fun existsByMail(mail: String) = QUser().mail.eq(mail).exists()
    fun existsByUsername(username: String) = QUser().username.eq(username).exists()

    fun createUser(userDTO: UserDTO, token: String = ""): User =
        User.from(userDTO, token).insertAndGet()

    fun editUser(id: Long, userDTO: UserDTO): UserInfo =
        getEntityById(id).merge(userDTO).updateAndGet().toInfo()

    fun registerUserActivity(id: Long) = getEntityById(id).registerNewActivity()

    fun deleteUserById(userId: Long) =
        QUser().id.eq(userId).delete()

    fun deleteUserByUsername(username: String) =
        QUser().username.eq(username).delete()

    fun getUserByUsername(username: String) : UserInfo? {
        return findByUsername(username)?.toInfo()
    }

    fun getUser(id: Long) : UserInfo =
        getEntityById(id).toInfo()

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
        if (!user.isTokenValid()) throw UnauthorizedException(UnauthorizedCause.INVALID_TOKEN)
        return user.verify().updateAndGet().toInfo()
    }

    fun isPasswordValid(id: Long, password: String): Boolean =
        isPasswordValid(password, getEntityById(id).passwordHash)

    fun isPasswordValid(password: String, hash: String): Boolean =
        BCrypt.verifyer().verify(password.toCharArray(), hash).verified

    fun updatePassword(id: Long, password: String) =
        getEntityById(id).updatePassword(password).updateAndGet().toInfo()

    fun resetPassword(token: String, password: String) {
        val user = findByToken(token)
        if (!user.isTokenValid()) throw UnauthorizedException(UnauthorizedCause.INVALID_TOKEN)
        user.useToken()
        user.updatePassword(password)
        user.update()
    }

    fun validateUser(id: Long) =
        getEntityById(id).verify().updateAndGet()

    fun updateSettings(id: Long, userSettingsDTO: UserSettingsDTO) =
        getEntityById(id).updateSettings(userSettingsDTO).updateAndGet().toInfo()

    fun search(searchString: String, paging: Paging): List<UserInfo> =
        QUser()
            .username.like("%$searchString%")
            .setPaging(paging)
            .findList()
            .map{it.toInfo()}


}