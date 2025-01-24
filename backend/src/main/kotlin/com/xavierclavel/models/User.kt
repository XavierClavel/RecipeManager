package com.xavierclavel.models

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.jointables.CookbookUser
import com.xavierclavel.models.jointables.Follow
import com.xavierclavel.models.jointables.Like
import common.dto.UserDTO
import common.infodto.UserInfo
import common.enums.UserRole
import common.enums.Visibility
import common.overviewdto.UserOverview
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
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

    //Privacy
    var accountVisibility : Visibility = Visibility.PUBLIC,
    var autoAcceptFollowRequestss : Boolean = false,

    var joinDate: Long = Instant.now().epochSecond,
    var lastActivityDate: Long = Instant.now().epochSecond,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    var recipes: Set<Recipe> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes : Set<Like> = setOf(),

    @OneToMany(mappedBy = "follower", cascade = [CascadeType.ALL], orphanRemoval = true)
    var follows: Set<Follow> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var followers: Set<Follow> = setOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var cookbooks: Set<CookbookUser> = setOf(),

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var dietaryRestrictions: DietaryRestrictions = DietaryRestrictions(),


    ): Model() {

    companion object {
        fun from(userDTO: UserDTO): User {
            val passwordHash = BCrypt.withDefaults().hashToString(12, userDTO.password.toCharArray())
            val mailHash = BCrypt.withDefaults().hashToString(12, userDTO.mail.toCharArray())

            return User(
                username = userDTO.username,
                passwordHash = passwordHash,
                mailHash = mailHash,
                role = userDTO.role,
            )
        }
    }

    fun registerNewActivity() =
        this.apply {
            lastActivityDate = Instant.now().epochSecond
        }

    fun toInfo() =
        UserInfo(
            id = this.id,
            username = this.username,
            role = this.role,
            joinDate = this.joinDate,
            bio = this.bio,
            recipesCount = this.recipes.size,
            likesCount = this.likes.size,
            cookbooksCount = this.cookbooks.size,
            followersCount = this.followers.size,
            followsCount = this.follows.size,
        )

    fun toOverview() = UserOverview(
        id = this.id,
        username = this.username,
    )

    fun merge(userDTO: UserDTO) = this.apply {
        username = userDTO.username
        bio = userDTO.bio
    }
}