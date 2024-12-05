package com.xavierclavel.models

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.jointables.CookbookUser
import com.xavierclavel.models.jointables.Follower
import com.xavierclavel.models.jointables.Like
import com.xavierclavel.utils.logger
import common.dto.UserDTO
import common.infodto.UserInfo
import common.enums.UserRole
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
class User (

    @Id
    var id: Long = 0,

    var username: String = "",

    var passwordHash: String = "",

    val mailHash: String = "",

    var role: UserRole = UserRole.USER,

    var bio: String = "",

    var joinDate: Long = Instant.now().epochSecond,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    var recipes: Set<Recipe> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes : Set<Like> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var follows: Set<Follower> = setOf(),

    @OneToMany(mappedBy = "follower", cascade = [CascadeType.ALL], orphanRemoval = true)
    var followedBy: Set<Follower> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cookbooks: Set<CookbookUser> = setOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="diet_id", referencedColumnName = "id")
    var dietaryRestrictions: DietaryRestrictions = DietaryRestrictions(),


    ): Model() {

    companion object {
        fun from(userDTO: UserDTO): User {
            val passwordHash = BCrypt.withDefaults().hashToString(12, userDTO.password.toCharArray())
            val mailHash = BCrypt.withDefaults().hashToString(12, userDTO.mail.toCharArray())
            logger.info {mailHash}

            return User(
                username = userDTO.username,
                passwordHash = passwordHash,
                mailHash = mailHash,
                role = userDTO.role,
            )
        }
    }

    fun toInfo() =
        UserInfo(
            id = this.id,
            username = this.username,
            role = this.role,
            joinDate = this.joinDate,
            bio = this.bio,
        )

    fun merge(userDTO: UserDTO) = this.apply {
        username = userDTO.username
        bio = userDTO.bio
    }
}