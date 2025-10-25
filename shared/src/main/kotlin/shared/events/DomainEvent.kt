package shared.events

import kotlinx.serialization.Serializable

@Serializable
sealed class DomainEvent {
    abstract val type: String
}
