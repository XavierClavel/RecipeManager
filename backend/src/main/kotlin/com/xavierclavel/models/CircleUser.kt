package com.xavierclavel.models

import common.CircleRole
import common.UserRole
import jakarta.persistence.Embedded
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table
class CircleUser (

    @Id
    @GeneratedValue
    var id: Long = -1,

    var title: String = "",

    @EmbeddedId
    @ManyToOne
    var user: User? =  null,

    @ManyToOne
    @EmbeddedId
    var circle: Circle? = null,

    var role: CircleRole = CircleRole.READER

)