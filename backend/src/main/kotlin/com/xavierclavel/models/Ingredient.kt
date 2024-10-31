package com.xavierclavel.models

import common.dto.IngredientDTO
import common.enums.IngredientType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import io.ebean.Model

@Entity
@Table(name = "ingredients")
class Ingredient (
    @Id
    @GeneratedValue
    var id: Long = -1,

    var name: String = "",

    var type: IngredientType = IngredientType.OTHER,
): Model() {
    companion object {
        fun from(ingredientDTO: IngredientDTO) = Ingredient(
            name = ingredientDTO.name,
            type = ingredientDTO.type,
        )
    }
}