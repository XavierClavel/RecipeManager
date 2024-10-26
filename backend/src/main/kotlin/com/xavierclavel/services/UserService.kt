package com.xavierclavel.services

import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.models.query.QUser
import org.koin.core.component.KoinComponent

class UserService: KoinComponent {
    fun countAll() =
        QUser().findCount()

    fun findById(userId: Long) : User? =
        QUser().id.eq(userId).findOne()

}