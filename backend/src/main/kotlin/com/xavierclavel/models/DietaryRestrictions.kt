package com.xavierclavel.models

import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "dietary_restrictions")
class DietaryRestrictions(
    @Id
    @GeneratedValue
    var id: Long = -1,

    var isMeatOk: Boolean = true,
    var isFishOk: Boolean = true,
    var isEggOk: Boolean = true,
    var isAlcoholOk: Boolean = true,
    var isPorkOk: Boolean = true,
    var isGlutenOk: Boolean = true,
    var isSugarOk: Boolean = true,
): Model()