package com.xavierclavel.models

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table
class Recipe (
    @Id
    @GeneratedValue
    var id: Long = -1,

    @ManyToOne
    var owner: User? = null,

    @ManyToMany
    var circles: MutableSet<Circle> = mutableSetOf(),

    var localId : Long = -1,

    var title: String = "",

    var description: String = "",

    var creationDate: Long = 0,

    var modificationDate: Long = 0,

    @ElementCollection
    var steps: List<String> = listOf(),
)