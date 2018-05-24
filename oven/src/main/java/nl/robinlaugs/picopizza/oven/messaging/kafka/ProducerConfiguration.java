package nl.robinlaugs.picopizza.oven.messaging.kafka;

import nl.robinlaugs.picopizza.routing.RoutingSlip;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * @author Robin Laugs
 */
@Configuration
public class ProducerConfiguration {

    @Value("${kafka.host}")
    private String host;

    @Bean
    public Map<String, Object> producerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, host);
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return properties;
    }

    @Bean
    public ProducerFactory<String, RoutingSlip> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProperties());
    }

    @Bean
    public KafkaTemplate<String, RoutingSlip> template() {
        return new KafkaTemplate<>(producerFactory());
    }

}