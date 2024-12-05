package com.xavierclavel.models.jointables

import com.xavierclavel.models.Cookbook
import com.xavierclavel.models.Recipe
import com.xavierclavel.models.User
import common.enums.CookbookRole
import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "cookbook_recipes")
class CookbookRecipe (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var recipe: Recipe,

    @ManyToOne
    var addedBy: User,

    @ManyToOne
    var cookbook: Cookbook,

    var additionDate: Long = Instant.now().epochSecond,

): Model()