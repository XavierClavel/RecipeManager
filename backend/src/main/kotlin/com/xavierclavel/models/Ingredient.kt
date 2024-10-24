package com.xavierclavel.models

import common.IngredientType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table
class Ingredient (
    @Id
    @GeneratedValue
    var id: Long = -1,

    var name: String = "",

    var type: IngredientType = IngredientType.OTHER,
)