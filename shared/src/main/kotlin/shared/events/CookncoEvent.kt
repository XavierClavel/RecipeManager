package shared.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CookncoEvent {
    abstract fun getTopic(): String
    open fun getKey(): String? = null
}

@Serializable
@SerialName("test")
data class TestEvent(val value: String): CookncoEvent() {
    override fun getTopic() = "test-topic"
}