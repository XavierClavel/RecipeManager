package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class UserEvent(): CookncoEvent() {
    override fun getTopic() = "cooknco-users"
}


@Serializable
@SerialName("user_created")
data class UserCreatedEvent(
    val id: Long,
    val username: String,
    val mail: String,
) : UserEvent() {
    override fun getKey() = id.toString()
}
