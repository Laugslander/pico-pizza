package nl.robinlaugs.picopizza.stock.messaging.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * @author Robin Laugs
 */
@Configuration
public class ProducerConfiguration {

    private static final String KAFKA_HOST = "localhost:9092";

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, String> template() {
        return new KafkaTemplate<>(producerFactory());
    }

}
