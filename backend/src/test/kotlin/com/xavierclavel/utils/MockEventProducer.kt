package main.com.xavierclavel.utils

import shared.events.CookncoEvent
import shared.events.EventProducer

class MockEventProducer : EventProducer {
    val eventsProduced = mutableListOf<CookncoEvent>()

    override fun produceEvent(event: () -> CookncoEvent) {
        eventsProduced.add(event())
    }
}