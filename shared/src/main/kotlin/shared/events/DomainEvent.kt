package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class DomainEvent {
    abstract val type: String
}

@Serializable
@SerialName("test")
data class TestEvent(val value: String): DomainEvent() {
    override val type = "test"
}