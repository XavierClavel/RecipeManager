package eu.cooknco.models

import io.ebean.Model
import jakarta.persistence.*
import shared.enums.Locale

@Entity
@Table(name = "users")
class User(
    @Id
    val id: Long = 0L,
    val username: String = "",
    val encryptedMail: String = "",
    val locale: Locale = Locale.FR,
    @Column(name = "notifications_enabled")
    val notificationsEnabled: Boolean = false,
): Model()