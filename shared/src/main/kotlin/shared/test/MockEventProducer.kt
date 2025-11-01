package shared.test

import shared.events.CookncoEvent
import shared.events.EventProducer

class MockEventProducer : EventProducer {
    val eventsProduced = mutableListOf<CookncoEvent>()

    override fun produceEvent(event: () -> CookncoEvent) {
        eventsProduced.add(event())
    }

    fun clear() = eventsProduced.clear()
}