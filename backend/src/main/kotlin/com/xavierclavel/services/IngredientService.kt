package com.xavierclavel.services

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.query.QIngredient
import common.dto.IngredientDTO
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import com.xavierclavel.utils.DbTransaction.deleteAndGet
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import common.infodto.IngredientInfo

class IngredientService: KoinComponent {
    fun countAll() =
        QIngredient().findCount()

    fun findById(ingredientId: Long) : IngredientInfo? =
        QIngredient().id.eq(ingredientId).findOne()?.toInfo()

    fun createIngredient(ingredientDTO: IngredientDTO): IngredientInfo =
        Ingredient().mergeDTO(ingredientDTO).insertAndGet().toInfo()

    fun updateIngredient(id:Long, ingredientDTO: IngredientDTO): IngredientInfo? =
        QIngredient().id.eq(id).findOne()
            ?.mergeDTO(ingredientDTO)
            ?.updateAndGet()
            ?.toInfo()



    fun updateIngredient(ingredientDTO: IngredientDTO) {

    }

    fun deleteById(ingredientId: Long): Boolean =
        QIngredient().id.eq(ingredientId).findOne().deleteAndGet() != null


    fun search(searchString: String, paging: Paging): List<IngredientInfo> =
        QIngredient()
            .name.like("%$searchString%")
            .setPaging(paging)
            .findList()
            .map{it.toInfo()}

    //Ebean.find(MyClass.class).order("id").findPagedList(page,size);

}