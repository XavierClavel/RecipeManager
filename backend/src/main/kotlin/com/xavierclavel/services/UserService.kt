package com.xavierclavel.services

import com.xavierclavel.models.DietaryRestrictions
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QUser
import common.dto.UserDTO
import io.ebean.DB
import org.koin.core.component.KoinComponent

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    fun findById(userId: Long) : User? =
        QUser().id.eq(userId).findOne()

    fun createUser(userDTO: UserDTO) {
        val diet = DietaryRestrictions()
        DB.getDefault().save(diet)
        DB.getDefault().save(User.from(userDTO, diet))
    }

    fun deleteUser(userId: Long) =
        QUser().id.eq(userId).delete()

    fun getUser(userId: Long) : UserDTO? {
        return findById(userId)?.toDTO()
    }

    fun listUsers(): List<UserDTO> =
        QUser().findList().map { it.toDTO() }

}