package com.xavierclavel.models

import common.dto.IngredientDTO
import common.enums.IngredientType
import common.infodto.IngredientInfo
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import io.ebean.Model
import io.ebean.annotation.DbDefault
import jakarta.persistence.GenerationType

@Entity
@Table(name = "ingredients")
class Ingredient (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String = "",

    var type: IngredientType = IngredientType.OTHER,

    var calories: Int = 0,
    var glucids: Float = 0f,
    var cholesterol: Float = 0f,
    var lipids: Float = 0f,
    var fibers: Float = 0f,
    var proteins: Float = 0f,
    var sodium: Float = 0f,

    @DbDefault(value = "true")
    var allowAmount: Boolean = true,

    @DbDefault(value = "true")
    var allowWeight: Boolean = true,

    @DbDefault(value = "true")
    var allowVolume: Boolean = true,

    @DbDefault(value = "1f")
    var volumicMass: Float = 1f,

    @DbDefault(value = "1f")
    var weightPerUnit: Float = 1f,
): Model() {

    fun mergeDTO(ingredientDTO: IngredientDTO): Ingredient {
        this.name = ingredientDTO.name
        this.type = ingredientDTO.type

        this.calories = ingredientDTO.calories
        this.glucids = ingredientDTO.glucids
        this.cholesterol = ingredientDTO.cholesterol
        this.lipids = ingredientDTO.lipids
        this.fibers = ingredientDTO.fibers
        this.proteins = ingredientDTO.proteins
        this.sodium = ingredientDTO.sodium

        this.allowAmount = ingredientDTO.allowAmount
        this.allowVolume = ingredientDTO.allowVolume
        this.allowVolume = ingredientDTO.allowVolume
        this.volumicMass = ingredientDTO.volumicMass
        this.weightPerUnit = ingredientDTO.weightPerUnit

        return this
    }

    fun toInfo() = IngredientInfo(
        id = this.id,
        name = this.name,
        type = this.type,

        calories = this.calories,
        glucids = this.glucids,
        cholesterol = this.cholesterol,
        lipids = this.lipids,
        fibers = this.fibers,
        proteins = this.proteins,
        sodium = this.sodium,
        allowAmount = this.allowAmount,
        allowWeight = this.allowWeight,
        allowVolume = this.allowVolume,
        volumicMass = this.volumicMass,
        weightPerUnit = this.weightPerUnit,



    )
}