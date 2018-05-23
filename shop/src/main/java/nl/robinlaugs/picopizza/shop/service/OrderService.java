package nl.robinlaugs.picopizza.shop.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.Payload;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.shop.data.OrderRepository;
import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.logging.Level.INFO;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class OrderService {

    @Value("${kafka.topic}")
    private String topic;

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, RoutingSlip> kafkaTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, RoutingSlip> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        Order order = get(slip.getPayload().getId());
        order.setReady(true);

        orderRepository.save(order);
    }

    public long create(Order order) {
        orderRepository.save(order);

        Payload payload = new Payload(order.getId(), order.getPizza().getIngredients());
        RoutingSlip slip = new RoutingSlip(payload);

        kafkaTemplate.send(topic, slip);

        return order.getId();
    }

    public Order get(long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

}
