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
): Model() {

    fun mergeDTO(ingredientDTO: IngredientDTO): Ingredient {
        this.name = ingredientDTO.name
        this.type = ingredientDTO.type

        return this
    }

    fun toInfo() = IngredientInfo(
        id = this.id,
        name = this.name,
        type = this.type,
    )
}