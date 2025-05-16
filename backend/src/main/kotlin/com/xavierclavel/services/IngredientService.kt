package com.xavierclavel.services

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.jointables.query.QRecipeIngredient
import com.xavierclavel.models.query.QIngredient
import common.dto.IngredientDTO
import io.ebean.Paging
import org.koin.core.component.KoinComponent
import com.xavierclavel.utils.DbTransaction.deleteAndGet
import com.xavierclavel.utils.DbTransaction.insertAndGet
import com.xavierclavel.utils.DbTransaction.updateAndGet
import common.enums.Locale
import common.infodto.IngredientInfo

class IngredientService: KoinComponent {
    fun countAll() =
        QIngredient().findCount()

    fun countRecipes(id: Long) =
        QRecipeIngredient().ingredient.id.eq(id).findCount()

    fun findEntityById(ingredientId: Long) : Ingredient? =
        QIngredient().id.eq(ingredientId).findOne()

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
        QIngredient().id.eq(ingredientId).findOne()?.deleteAndGet() != null


    fun search(searchString: String, paging: Paging, locale: Locale): Pair<Int,List<IngredientInfo>> {
        val query = QIngredient()
            .apply {
                when(locale) {
                    Locale.EN -> this.name_en.ilike("%$searchString%")
                    Locale.FR -> this.name_fr.ilike("%$searchString%")
                }
            }
        return Pair(query.findCount(), query.setPaging(paging).findList().map{it.toInfo()})
    }



    //Ebean.find(MyClass.class).order("id").findPagedList(page,size);

}