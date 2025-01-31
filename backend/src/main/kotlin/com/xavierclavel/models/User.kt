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
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.bouncycastle.cms.RecipientId.password
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class User (

    @Id
    var id: Long = 0,

    @Column(unique = true)
    var username: String = "",

    @Column(unique = true)
    val mail: String = "",

    var role: UserRole = UserRole.USER,

    var passwordHash: String = "",

    var bio: String = "",

    //Validation
    @Column(unique = true)
    var verificationToken: String = "",
    var isVerified: Boolean = false,

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
        fun from(userDTO: UserDTO, token: String): User {
            val passwordHash = BCrypt.withDefaults().hashToString(12, userDTO.password.toCharArray())
            return User(
                username = userDTO.username,
                mail = userDTO.mail,
                role = userDTO.role,
                passwordHash = passwordHash,
                verificationToken = token,
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

    fun validate() = this.apply {
        isVerified = true
    }

    fun updatePassword(password: String) = this.apply {
        passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun updateToken(token: String) = this.apply {
        verificationToken = token
    }
}