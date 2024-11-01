package com.xavierclavel.services

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.query.QIngredient
import common.dto.IngredientDTO
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import com.xavierclavel.utils.DbTransaction.deleteAndGet
import com.xavierclavel.utils.DbTransaction.insertAndGet

class IngredientService: KoinComponent {
    fun countAll() =
        QIngredient().findCount()

    fun findById(ingredientId: Long) : IngredientDTO? =
        QIngredient().id.eq(ingredientId).findOne()?.toInfo()

    fun createIngredient(ingredientDTO: IngredientDTO): IngredientDTO =
        Ingredient().mergeDTO(ingredientDTO).insertAndGet().toInfo()

    fun updateIngredient(id:Long, ingredientDTO: IngredientDTO): IngredientDTO? =
        QIngredient().id.eq(id).findOne()
            ?.mergeDTO(ingredientDTO)
            ?.insertAndGet()
            ?.toInfo()



    fun updateIngredient(ingredientDTO: IngredientDTO) {

    }

    fun deleteById(ingredientId: Long): Boolean =
        QIngredient().id.eq(ingredientId).findOne().deleteAndGet() != null


    fun search(searchString: String, paging: Paging): List<Ingredient> =
        QIngredient()
            .name.like("%$searchString%")
            .setPaging(paging)
            .findList()

    //Ebean.find(MyClass.class).order("id").findPagedList(page,size);

}