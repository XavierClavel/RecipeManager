package com.xavierclavel.models

import com.xavierclavel.models.localization.LocalizedIngredientName
import shared.dto.IngredientDTO
import shared.enums.IngredientType
import shared.infodto.IngredientInfo
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.OneToMany
import io.ebean.Model
import io.ebean.annotation.DbDefault
import jakarta.persistence.CascadeType
import jakarta.persistence.GenerationType

@Entity
@Table(name = "ingredients")
class Ingredient (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToMany(mappedBy = "ingredient", cascade = [CascadeType.ALL])
    var translations: MutableList<LocalizedIngredientName> = mutableListOf(),

    var type: IngredientType = IngredientType.MISCELLANEOUS,

    var calories: Int = 0,

    var cholesterol: Float = 0f,

    @DbDefault(value = "0")
    var saturatedFat: Float = 0f,

    @DbDefault(value = "0")
    var unsaturatedFat: Float = 0f,

    @DbDefault(value = "0")
    var carbohydrates: Float = 0f,

    @DbDefault(value = "0")
    var sugars: Float = 0f,

    var fibers: Float = 0f,
    var proteins: Float = 0f,
    var sodium: Float = 0f,

    @DbDefault(value = "true")
    var allowAmount: Boolean = true,

    @DbDefault(value = "true")
    var allowWeight: Boolean = true,

    @DbDefault(value = "true")
    var allowVolume: Boolean = true,

    @DbDefault(value = "1.0")
    var volumicMass: Float = 1f,

    @DbDefault(value = "1.0")
    var weightPerUnit: Float = 1f,

    ): Model() {

    fun mergeDTO(ingredientDTO: IngredientDTO): Ingredient {
        this.type = ingredientDTO.type

        this.calories = ingredientDTO.calories
        this.carbohydrates = ingredientDTO.carbohydrates
        this.cholesterol = ingredientDTO.cholesterol
        this.saturatedFat = ingredientDTO.saturatedFat
        this.unsaturatedFat = ingredientDTO.unsaturatedFat
        this.fibers = ingredientDTO.fibers
        this.sugars = ingredientDTO.sugars
        this.proteins = ingredientDTO.proteins
        this.sodium = ingredientDTO.sodium

        this.allowAmount = ingredientDTO.allowAmount
        this.allowVolume = ingredientDTO.allowVolume
        this.allowWeight = ingredientDTO.allowWeight
        this.volumicMass = ingredientDTO.volumicMass
        this.weightPerUnit = ingredientDTO.weightPerUnit

        return this
    }

    fun toInfo() = IngredientInfo(
        id = this.id,
        type = this.type,

        calories = this.calories,
        carbohydrates = this.carbohydrates,
        cholesterol = this.cholesterol,
        saturatedFat = this.saturatedFat,
        unsaturatedFat = this.unsaturatedFat,
        sugars = this.sugars,
        fibers = this.fibers,
        proteins = this.proteins,
        sodium = this.sodium,
        allowAmount = this.allowAmount,
        allowWeight = this.allowWeight,
        allowVolume = this.allowVolume,
        volumicMass = this.volumicMass,
        weightPerUnit = this.weightPerUnit,

        name = translations.associate { it.locale to it.name },
    )
}