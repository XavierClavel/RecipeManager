package shared.events

import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import java.lang.Exception
import java.time.Duration
import kotlin.collections.mapOf

class KafkaEventConsumer(
    private val groupId: String,
    private val topics: List<String>,
    private val handle: (DomainEvent) -> Unit,
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val consumer = KafkaConsumer<String, String>(
        mapOf(
            org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS"),
            org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG to groupId,
            org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        )
    )

    fun start() {
        consumer.subscribe(topics)
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
                try {
                    val event = json.decodeFromString<DomainEvent>(record.value())
                    handle(event)
                } catch (e: Exception) {
                    println("Failed to process event: ${e.message}")
                }
            }
            consumer.commitAsync()
        }
    }
}
