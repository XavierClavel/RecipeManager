package shared.events

import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import shared.utils.logger
import java.time.Duration

class KafkaEventProducer {
    private val json = Json { ignoreUnknownKeys = true }

    private val producer = KafkaProducer<String, String>(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS"),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringSerializer",
        )
    )

    fun produceEvent(topic: String, key: String? = null, event: () -> DomainEvent) {
        val data = json.encodeToString(event())
        val record = ProducerRecord("recipe-events", key, data)
        producer.send(record)
    }

}

class KafkaProducerService: KoinComponent {
    val producer = KafkaEventProducer()

    fun produceEvent(topic: String, key: String? = null, event: () -> DomainEvent) =
        producer.produceEvent(topic, key, event)
}