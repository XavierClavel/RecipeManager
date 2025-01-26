package com.xavierclavel.models.jointables

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.Recipe
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
@Table(name = "recipe_ingredients")
class RecipeIngredient (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var recipe: Recipe? =  null,

    @ManyToOne
    var ingredient: Ingredient? = null,

    var amount: Float? = null,
    var unit: AmountUnit = AmountUnit.NONE,
    var complement: String? = null,

    ): Model() {
    fun mergeDTO(recipeIngredientDTO: RecipeDTO.RecipeIngredientDTO) = this.apply {
        this.amount = recipeIngredientDTO.amount
        this.unit = recipeIngredientDTO.unit
        this.complement = recipeIngredientDTO.complement
    }

    fun toInfo() = RecipeIngredientInfo(
        id = ingredient!!.id,
        name = ingredient!!.name,
        amount = amount,
        unit = unit,
        complement = complement,
    )

    fun compareTo(dto: RecipeDTO.RecipeIngredientDTO): Boolean {
        if (this.amount == null && dto.amount != null) return false
        if (this.amount != null && dto.amount == null) return false
        if (this.amount != null && dto.amount != null && Math.abs(this.amount!! - dto.amount!!) > 0.0001) return false
        if (this.unit != dto.unit) return false
        if (this.complement != dto.complement) return false

        return this.ingredient!!.id == dto.id &&
            this.unit == dto.unit
    }


}