package com.xavierclavel.models.jointables

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import common.dto.RecipeDTO
import common.enums.AmountUnit
import common.infodto.RecipeIngredientInfo
import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "recipe_ingredients", uniqueConstraints = [UniqueConstraint(columnNames = ["recipe_id", "ingredient_id"])])
class RecipeIngredient (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var recipe: Recipe? =  null,

    @ManyToOne
    var ingredient: Ingredient? = null,

    var amount: Float = -1f,
    var unit: AmountUnit = AmountUnit.NONE,

): Model() {
    fun mergeDTO(recipeIngredientDTO: RecipeDTO.RecipeIngredientDTO) = this.apply {
        this.amount = recipeIngredientDTO.amount
        this.unit = recipeIngredientDTO.unit
    }

    fun toInfo() = RecipeIngredientInfo(
        id = id,
        name = ingredient!!.name,
        amount = amount,
        unit = unit,
    )
}