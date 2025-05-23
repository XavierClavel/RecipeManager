package com.xavierclavel.services

import com.xavierclavel.exceptions.NotFoundCause
import com.xavierclavel.exceptions.NotFoundException
import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.jointables.query.QRecipeIngredient
import com.xavierclavel.models.localization.LocalizedIngredientName
import com.xavierclavel.models.localization.query.QLocalizedIngredientName
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

    fun createIngredient(ingredientDTO: IngredientDTO): IngredientInfo {
        val ingredient = Ingredient().mergeDTO(ingredientDTO).insertAndGet()
        ingredientDTO.name.forEach {
            val localizedIngredientName = LocalizedIngredientName(ingredient = ingredient, locale = it.key, name = it.value)
            localizedIngredientName.insert()
        }
        return QIngredient().id.eq(ingredient.id).findOne()!!.toInfo()
    }


    fun updateIngredient(id:Long, ingredientDTO: IngredientDTO): IngredientInfo {
        val ingredient = findEntityById(id) ?: throw NotFoundException(NotFoundCause.INGREDIENT_NOT_FOUND)

        ingredientDTO.name.forEach {
            val localizedIngredientName = QLocalizedIngredientName()
                .ingredient.id.eq(ingredient.id)
                .locale.eq(it.key)
                .findOneOrEmpty()
                .orElse(LocalizedIngredientName(ingredient = ingredient, locale = it.key))
            localizedIngredientName.name = it.value
            localizedIngredientName.save()
        }
        ingredient
            .mergeDTO(ingredientDTO)
            .updateAndGet()
            .toInfo()
        return QIngredient().id.eq(ingredient.id).findOne()!!.toInfo()
    }



    fun updateIngredient(ingredientDTO: IngredientDTO) {

    }

    fun deleteById(ingredientId: Long): Boolean =
        QIngredient().id.eq(ingredientId).findOne()?.deleteAndGet() != null


    fun search(searchString: String, paging: Paging, locale: Locale): Pair<Int,List<IngredientInfo>> {
        val query = QIngredient()
            .and()
            .translations.locale.eq(locale)
            .translations.name.ilike("%$searchString%")
            .endAnd()

        return Pair(query.findCount(), query.setPaging(paging).findList().map{it.toInfo()})
    }



    //Ebean.find(MyClass.class).order("id").findPagedList(page,size);

}