import jakarta.persistence.*
import shared.enums.Locale
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    val id: Long = 0L,
    val username: String = "",
    val email: String = "",
    val locale: Locale = Locale.EN,
    @Column(name = "notifications_enabled")
    val notificationsEnabled: Boolean = false,
)

// Follower.kt
@Entity
@Table(name = "followers")
class Follower(
    @Id
    val id: Long,
    @Column(name = "follower_id")
    val followerId: Long,
    @Column(name = "followed_id")
    val followedId: Long,
)
