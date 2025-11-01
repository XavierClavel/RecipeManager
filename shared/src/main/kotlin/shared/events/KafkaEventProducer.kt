package shared.events

import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent

interface EventProducer: KoinComponent {
    fun produceEvent(event: () -> CookncoEvent)
}

class KafkaEventProducer: EventProducer {
    private val json = Json { ignoreUnknownKeys = true }

    private val producer by lazy {
        KafkaProducer<String, String>(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS")
                    ?: throw IllegalStateException("KAFKA_BOOTSTRAP_SERVERS not set"),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            )
        )
    }

    override fun produceEvent(event: () -> CookncoEvent) {
        val message = event()
        val data = json.encodeToString(message)
        val record = ProducerRecord(message.getTopic(), message.getKey(), data)
        producer.send(record)
    }

}