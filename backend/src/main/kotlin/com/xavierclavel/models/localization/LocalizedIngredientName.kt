package com.xavierclavel.models.localization

import com.xavierclavel.models.Ingredient
import shared.enums.Locale
import io.ebean.Model
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "ingredient_translation",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["ingredient_id", "locale"])
    ]
)
class LocalizedIngredientName (

    @Id
    var id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    var ingredient: Ingredient? = null,

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "locale", nullable = false)
    var locale: Locale = Locale.EN,

): Model()