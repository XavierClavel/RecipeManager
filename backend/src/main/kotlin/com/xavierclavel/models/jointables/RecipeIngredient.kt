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
@Table(name = "recipe_ingredients")
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

    )