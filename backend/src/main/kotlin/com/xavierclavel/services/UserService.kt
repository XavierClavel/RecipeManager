package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QRecipe
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
        DB.getDefault().save(User.from(userDTO))
    }

    fun getUser(userId: Long) : UserDTO? {
        return findById(userId)?.toDTO()
    }

}