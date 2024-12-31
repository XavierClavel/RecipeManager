package com.xavierclavel.models

import common.dto.IngredientDTO
import common.enums.IngredientType
import common.infodto.IngredientInfo
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import io.ebean.Model
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

        return this
    }

    fun toInfo() = IngredientInfo(
        id = this.id,
        name = this.name,
        type = this.type,
    )
}