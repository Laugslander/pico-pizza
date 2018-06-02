package nl.robinlaugs.picopizza.oven.messaging.kafka;

import nl.robinlaugs.picopizza.routing.Action;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static nl.robinlaugs.picopizza.routing.Action.*;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * @author Robin Laugs
 */
@EnableKafka
@Configuration
public class ConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(GROUP_ID_CONFIG, consumerGroupId);

        return properties;
    }

    @Bean
    public ConsumerFactory<String, RoutingSlip> consumerFactory() {
        StringDeserializer keyDeserializer = new StringDeserializer();

        JsonDeserializer<RoutingSlip> valueDeserializer = new JsonDeserializer<>(RoutingSlip.class);
        valueDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(consumerProperties(), keyDeserializer, valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RoutingSlip> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RoutingSlip> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        factory.setRecordFilterStrategy(r -> {
            RoutingSlip slip = r.value();

            Action stockActionStatus = slip.getStockActionStatus();
            Action ovenActionStatus = slip.getOvenActionStatus();

            boolean stockActionCondition = stockActionStatus.equals(TODO) || stockActionStatus.equals(STOP);
            boolean ovenActionCondition = ovenActionStatus.equals(CONTINUE) || ovenActionStatus.equals(STOP);

            return stockActionCondition || ovenActionCondition;
        });

        return factory;
    }

}
