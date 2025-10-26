package models

import io.ebean.Model
import jakarta.persistence.*

@Entity
@Table(name = "followers")
class Follower(
    @Id
    val id: Long = 0,
    @Column(name = "follower_id")
    val followerId: Long = 0,
    @Column(name = "followed_id")
    val followedId: Long = 0,
): Model()
