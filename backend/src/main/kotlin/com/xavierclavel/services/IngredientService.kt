package com.xavierclavel.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.User
import com.xavierclavel.models.query.QIngredient
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.logger
import common.dto.IngredientDTO
import common.dto.UserDTO
import common.dto.UserInfo
import common.enums.UserRole
import io.ebean.Paging
import org.koin.core.component.KoinComponent

class IngredientService: KoinComponent {
    fun countAll() =
        QIngredient().findCount()

    fun findById(ingredientId: Long) : Ingredient? =
        QIngredient().id.eq(ingredientId).findOne()

    fun createIngredient(ingredientDTO: IngredientDTO) {
        Ingredient.from(ingredientDTO).insert()
    }

    fun search(searchString: String, paging: Paging): List<Ingredient> =
        QIngredient()
            .name.like("%$searchString%")
            .setPaging(paging)
            .findList()

    //Ebean.find(MyClass.class).order("id").findPagedList(page,size);

}