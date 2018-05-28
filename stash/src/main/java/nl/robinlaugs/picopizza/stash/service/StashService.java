package nl.robinlaugs.picopizza.stash.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.stash.data.redis.OrderRepository;
import nl.robinlaugs.picopizza.stash.domain.Order;
import nl.robinlaugs.picopizza.stash.exception.PayloadNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.routing.Action.CONTINUE;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class StashService {

    @Value("${kafka.routing-topic}")
    private String routingTopic;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    private final OrderRepository orderRepository;

    @Autowired
    public StashService(KafkaTemplate<String, RoutingSlip> kafka, OrderRepository orderRepository) {
        this.kafka = kafka;
        this.orderRepository = orderRepository;
    }

    @KafkaListener(topics = "${kafka.routing-topic}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        slip.setStashActionStatus(CONTINUE);

        String id = String.valueOf(slip.getPayload().getId());
        Order order = new Order(id);

        orderRepository.save(order);

        kafka.send(routingTopic, slip);
    }

    public Order get(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new PayloadNotFoundException(id));

        orderRepository.deleteById(id);

        return order;
    }
}
