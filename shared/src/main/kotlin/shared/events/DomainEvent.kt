package shared.events

import kotlinx.serialization.Serializable

@Serializable
sealed class DomainEvent {
    abstract val type: String
}

@Serializable
sealed class TestEvent: DomainEvent() {
    override val type = "test"
}