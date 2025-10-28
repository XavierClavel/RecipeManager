package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class UserEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-users"
}


@Serializable
data class UserCreatedEvent(
    val id: Long,
    val username: String,
    val mail: String,
) : UserEvent() {
    override val type = "user_created"
    override fun getKey() = id.toString()
}
