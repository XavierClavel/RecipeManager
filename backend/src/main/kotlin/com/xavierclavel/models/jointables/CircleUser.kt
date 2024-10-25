package com.xavierclavel.models.jointables

import com.xavierclavel.models.Circle
import com.xavierclavel.models.User
import common.enums.CircleRole
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "circle_users")
class CircleUser (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,

    @ManyToOne
    var user: User? =  null,

    @ManyToOne
    var circle: Circle? = null,

    var role: CircleRole = CircleRole.READER

)