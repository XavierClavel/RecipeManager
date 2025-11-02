package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-settings"
}

@Serializable
@SerialName("notifications_toggled")
data class NotificationsToggledEvent(
    val userId: Long,
    val enabled: Boolean
) : SettingsEvent() {
    override fun getKey() = userId.toString()
}
