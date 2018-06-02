package nl.robinlaugs.picopizza.shop.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.Payload;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.shop.data.OrderRepository;
import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.domain.OrderStatus;
import nl.robinlaugs.picopizza.shop.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.shop.domain.OrderStatus.*;
import static nl.robinlaugs.picopizza.shop.messaging.MessagingConstants.ROUTING_TOPIC;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class ShopService {

    private static final String STASH_SERVICE_API = "http://localhost:8083/stash/";

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    private RestTemplate rest;

    @Autowired
    public ShopService(OrderRepository orderRepository, KafkaTemplate<String, RoutingSlip> kafka) {
        this.orderRepository = orderRepository;
        this.kafka = kafka;

        rest = new RestTemplate();
    }

    @KafkaListener(topics = ROUTING_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        Payload payload = slip.getPayload();
        Long id = payload.getId();

        log.log(INFO, format("Received routing slip for order %d", id));

        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(payload.isInStock() ? PREPARED : OUT_OF_STOCK);

        orderRepository.save(order);
    }

    public long create(Order order) {
        orderRepository.save(order);

        long id = order.getId();

        log.log(INFO, "Created order with id %d", id);

        Payload payload = new Payload(id, order.getPizza().getIngredients());
        RoutingSlip slip = new RoutingSlip(payload);

        kafka.send(ROUTING_TOPIC, slip);

        log.log(INFO, format("Sent routing slip for order %d to topic", id));

        return id;
    }

    public Order get(long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        OrderStatus status = order.getStatus();

        if (status.equals(BEING_PREPARED) || status.equals(OUT_OF_STOCK) || status.equals(SERVED)) {
            log.log(INFO, format("User requested order with id %d, status is %s", id, status));

            return order;
        }

        boolean inStash = rest.getForObject(format("%s%d", STASH_SERVICE_API, order.getId()), Boolean.class);

        if (inStash) {
            order.setStatus(SERVED);

            orderRepository.save(order);
        }

        log.log(INFO, format("User requested order with id %d, status is %s", id, status));

        return order;
    }

}
