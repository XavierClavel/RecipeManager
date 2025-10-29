package models

import io.ebean.Model
import jakarta.persistence.*

@Entity
@Table(name = "followers")
class Follower(
    @Id
    val id: Long = 0,

    @ManyToOne
    var follower: User? = null,

    @ManyToOne
    var followed: User? = null,
): Model()
