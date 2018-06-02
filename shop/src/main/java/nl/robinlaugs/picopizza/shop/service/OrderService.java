package nl.robinlaugs.picopizza.shop.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.Payload;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.shop.data.OrderRepository;
import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.shop.domain.OrderStatus.OUT_OF_STOCK;
import static nl.robinlaugs.picopizza.shop.domain.OrderStatus.PREPARED;
import static nl.robinlaugs.picopizza.shop.messaging.MessagingConstants.ROUTING_TOPIC;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class OrderService {

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, RoutingSlip> kafka) {
        this.orderRepository = orderRepository;
        this.kafka = kafka;
    }

    @KafkaListener(topics = ROUTING_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        Payload payload = slip.getPayload();

        Order order = get(payload.getId());
        order.setStatus(payload.isInStock() ? PREPARED : OUT_OF_STOCK);

        orderRepository.save(order);
    }

    public long create(Order order) {
        orderRepository.save(order);

        long id = order.getId();

        Payload payload = new Payload(id, order.getPizza().getIngredients());
        RoutingSlip slip = new RoutingSlip(payload);

        log.log(INFO, format("Sent routing slip for order %d to topic", id));

        kafka.send(ROUTING_TOPIC, slip);

        return id;
    }

    public Order get(long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

}
