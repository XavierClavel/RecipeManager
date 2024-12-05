package com.xavierclavel.models.jointables

import com.xavierclavel.models.Cookbook
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
@Table(name = "cookbook_users")
class CookbookUser (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne
    var user: User? =  null,

    @ManyToOne
    var cookbook: Cookbook? = null,

    var role: CookbookRole = CookbookRole.READER,

    var pending: Boolean = false,

    var joinDate: Long = Instant.now().epochSecond,

): Model()