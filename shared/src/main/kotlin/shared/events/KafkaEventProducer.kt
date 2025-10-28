package shared.events

import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent

class KafkaEventProducer {
    private val json = Json { ignoreUnknownKeys = true }

    private val producer = KafkaProducer<String, String>(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS"),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
        )
    )

    fun produceEvent(event: () -> CookncoEvent) {
        val message = event()
        val data = json.encodeToString(message)
        val record = ProducerRecord(message.getTopic(), message.getKey(), data)
        producer.send(record)
    }

}

class KafkaProducerService: KoinComponent {
    val producer = KafkaEventProducer()

    fun produceEvent(event: () -> CookncoEvent) =
        producer.produceEvent(event)
}