package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-auth"
}


@Serializable
@SerialName("password_reset_requested")
data class PasswordResetRequestedEvent(
    val userId: Long,
    val token: String,
) : AuthEvent() {
    override val type = "password_reset_requested"
}


@Serializable
@SerialName("account_verification_requested")
data class AccountVerificationRequestedEvent(
    val userId: Long,
    val token: String,
) : AuthEvent() {
    override val type = "account_verification_requested"
}
