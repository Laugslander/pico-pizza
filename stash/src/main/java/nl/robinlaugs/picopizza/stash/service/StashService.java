package nl.robinlaugs.picopizza.stash.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.stash.data.redis.OrderRepository;
import nl.robinlaugs.picopizza.stash.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.routing.Action.CONTINUE;
import static nl.robinlaugs.picopizza.stash.messaging.MessagingConstants.ROUTING_TOPIC;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class StashService {

    private final KafkaTemplate<String, RoutingSlip> kafka;

    private final OrderRepository orderRepository;

    @Autowired
    public StashService(KafkaTemplate<String, RoutingSlip> kafka, OrderRepository orderRepository) {

        this.kafka = kafka;
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = ROUTING_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        slip.setStashActionStatus(CONTINUE);

        String id = String.valueOf(slip.getPayload().getId());
        Order order = new Order(id);

        orderRepository.save(order);

        kafka.send(ROUTING_TOPIC, slip);

        log.log(INFO, format("Sent routing slip for order %s to topic", id));
    }

    public boolean get(String id) {
        Order order = orderRepository.findById(id).orElse(null);

        log.log(INFO, format("Shop service requested order with id %s", id));

        if (order != null) {
            orderRepository.deleteById(id);

            log.log(INFO, format("Removed order with id %s from stash", id));

            return true;
        }

        return false;
    }

}
