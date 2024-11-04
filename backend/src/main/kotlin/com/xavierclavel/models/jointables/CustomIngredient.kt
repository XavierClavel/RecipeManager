package com.xavierclavel.models.jointables

import com.xavierclavel.models.Ingredient
import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import common.enums.AmountUnit
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

    var ingredient: String = "",
    var amount: Float = -1f,
    var unit: AmountUnit = AmountUnit.NONE,

    )