package com.xavierclavel.models.jointables

import com.xavierclavel.models.Recipe
import shared.dto.RecipeDTO
import shared.enums.AmountUnit
import shared.infodto.CustomIngredientInfo
import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "custom_ingredients")
class CustomIngredient (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var recipe: Recipe? =  null,

    var name: String = "",
    var amount: Float? = null,
    var unit: AmountUnit = AmountUnit.NONE,

    ): Model() {
    fun mergeDTO(customIngredientDTO: RecipeDTO.CustomIngredientDTO) = this.apply {
        this.amount = customIngredientDTO.amount
        this.unit = customIngredientDTO.unit
    }

    fun toInfo() = CustomIngredientInfo(
        name = name,
        amount = amount,
        unit = unit,
    )
}