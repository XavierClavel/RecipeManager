package com.xavierclavel.models

import com.xavierclavel.models.jointables.Follower
import com.xavierclavel.models.jointables.Like
import common.dto.UserDTO
import common.enums.UserRole
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User (

    @Id
    var id: Long = 0,

    var username: String = "",

    var role: UserRole = UserRole.USER,

    @OneToMany
    var recipes: Set<Recipe> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var likes : Set<Like> = setOf(),

    @OneToMany(mappedBy = "user")
    var follows: Set<Follower> = setOf(),

    @OneToMany(mappedBy = "follower")
    var followedBy: Set<Follower> = setOf(),

    @ManyToMany
    var circles: Set<Circle> = setOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="diet_id", referencedColumnName = "id")
    var dietaryRestrictions: DietaryRestrictions = DietaryRestrictions(),


): Model() {

    companion object {
        fun from(userDTO: UserDTO) =
            User(
                username = userDTO.username,
                role = userDTO.role,
            )
    }

    fun toDTO() =
        UserDTO(
            username = username,
            role = role,
        )
}