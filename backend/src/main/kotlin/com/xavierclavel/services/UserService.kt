package com.xavierclavel.services

import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import common.dto.UserDTO
import common.enums.UserRole
import org.koin.core.component.KoinComponent

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    fun findById(userId: Long) : User? =
        QUser().id.eq(userId).findOne()

    fun findByUsername(username: String) : User? =
        QUser().username.eq(username).findOne()

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

    fun getUserByUsername(username: String) : UserDTO? {
        return findByUsername(username)?.toDTO()
    }

    fun listUsers(): List<UserDTO> =
        QUser().findList().map { it.toDTO() }

    //TODO :parameterize password
    fun setupDefaultAdmin() {
        if (findByUsername("admin") != null) return
        createUser(UserDTO(
            username = "admin",
            password = "Passw0rd",
            role = UserRole.ADMIN,
        ))
    }

}