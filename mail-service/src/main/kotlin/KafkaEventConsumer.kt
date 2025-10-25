import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import java.time.Duration

class KafkaEventConsumer(
    private val processor: EventProcessor
) {
    private val json = Json { ignoreUnknownKeys = true }

    private val consumer = KafkaConsumer<String, String>(
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to System.getenv("KAFKA_BOOTSTRAP_SERVERS"),
            ConsumerConfig.GROUP_ID_CONFIG to "mail-service",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        )
    )

    fun start() {
        consumer.subscribe(listOf("user-events", "recipe-events"))
        while (true) {
            val records = consumer.poll(Duration.ofSeconds(1))
            for (record in records) {
                try {
                    val event = json.decodeFromString<DomainEvent>(record.value())
                    processor.handle(event)
                } catch (e: Exception) {
                    println("Failed to process event: ${e.message}")
                }
            }
            consumer.commitAsync()
        }
    }
}
