package com.xavierclavel.models

import at.favre.lib.crypto.bcrypt.BCrypt
import com.xavierclavel.models.jointables.CookbookUser
import com.xavierclavel.models.jointables.Follow
import com.xavierclavel.models.jointables.Like
import common.dto.UserDTO
import common.dto.UserSettingsDTO
import common.infodto.UserInfo
import common.enums.UserRole
import common.overviewdto.UserOverview
import io.ebean.Model
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.ZoneOffset

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
    var token: String = "",
    var tokenEndValidity: LocalDateTime = LocalDateTime.now().plusDays(1),
    var isVerified: Boolean = false,

    //Privacy
    var isAccountPublic: Boolean = true,
    var autoAcceptFollowRequests : Boolean = false,

    var joinDate: LocalDateTime = LocalDateTime.now(),
    var lastActivityDate: LocalDateTime = LocalDateTime.now(),

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
                token = token,
            )
        }
    }

    fun registerNewActivity() =
        this.apply {
            lastActivityDate = LocalDateTime.now()
        }

    fun toInfo() =
        UserInfo(
            id = this.id,
            username = this.username,
            role = this.role,
            joinDate = this.joinDate.toEpochSecond(ZoneOffset.UTC),
            bio = this.bio,
            recipesCount = this.recipes.size,
            likesCount = this.likes.size,
            cookbooksCount = this.cookbooks.size,
            followersCount = this.followers.count { !it.pending },
            followsCount = this.follows.count { !it.pending },
        )

    fun toOverview() = UserOverview(
        id = this.id,
        username = this.username,
    )

    fun merge(userDTO: UserDTO) = this.apply {
        username = userDTO.username
        bio = userDTO.bio
    }

    fun verify() = this.apply {
        isVerified = true
    }

    fun updatePassword(password: String) = this.apply {
        passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun updateSettings(userSettingsDTO: UserSettingsDTO) = this.apply {
        autoAcceptFollowRequests = userSettingsDTO.autoAcceptFollowRequests
        isAccountPublic = userSettingsDTO.isAccountPublic
    }

    fun getSettings() = UserSettingsDTO(
        autoAcceptFollowRequests = this.autoAcceptFollowRequests,
        isAccountPublic = this.isAccountPublic,
    )

    fun useToken() {
        this.apply { tokenEndValidity = LocalDateTime.now().minusDays(1) }.update()
    }

    fun isTokenValid(): Boolean {
        return LocalDateTime.now() < this.tokenEndValidity
    }

    fun updateToken(token:String) {
        this.token = token
        this.tokenEndValidity = LocalDateTime.now().plusDays(1)
        update()
    }
}