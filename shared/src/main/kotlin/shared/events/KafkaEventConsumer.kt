package shared.events

import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import shared.utils.logger
import java.time.Duration

class KafkaEventConsumer(
    private val groupId: String,
    private val topics: List<String>,
    private val handle: (CookncoEvent) -> Unit,
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val consumer = KafkaConsumer<String, String>(
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS"),
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        )
    )

    fun start() {
        consumer.subscribe(topics)
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
                try {
                    logger.info { "Received record: ${record.value()}" }
                    val event = json.decodeFromString<CookncoEvent>(record.value())
                    handle(event)
                } catch (e: Exception) {
                    logger.error{"Failed to process event: ${e.message}"}
                }
            }
            consumer.commitAsync()
        }
    }
}
