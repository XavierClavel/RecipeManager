package com.xavierclavel.models

import com.xavierclavel.models.jointables.CookbookUser
import common.dto.CookbookDTO
import common.infodto.CookbookInfo
import io.ebean.Model
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table
class Cookbook (

    @Id
    @GeneratedValue
    var id: Long = 1,

    var title: String = "",

    var description: String = "",

    var creationDate: Long = Instant.now().epochSecond,

    @ManyToMany
    var recipes: MutableSet<Recipe> = mutableSetOf(),

    @ManyToMany
    var users: MutableSet<CookbookUser> = mutableSetOf(),

    var isPublic: Boolean = false,
): Model() {
    companion object {
        fun from(cookbookDTO: CookbookDTO)= Cookbook(
            title = cookbookDTO.title,
            description = cookbookDTO.description,
        )
    }

    fun toInfo() = CookbookInfo(
        id = this.id,
        title = this.title,
        description = this.description,
    )

}