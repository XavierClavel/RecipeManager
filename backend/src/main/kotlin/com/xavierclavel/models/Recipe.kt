package com.xavierclavel.models

import com.xavierclavel.models.jointables.Like
import jakarta.persistence.CascadeType
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "recipes")
class Recipe (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    var localId : Long = -1,

    var title: String = "",

    var description: String = "",

    var creationDate: Long = 0,

    var modificationDate: Long = 0,

    @ElementCollection
    var steps: List<String> = listOf(),

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var ingredients: List<Ingredient> = listOf(),


    @ManyToOne
    var owner: User? = null,

    @ManyToMany
    var circles: MutableSet<Circle> = mutableSetOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
    var likes: MutableSet<Like> = mutableSetOf(),

)