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

    private final KafkaTemplate<String, RoutingSlip> kafka;

    @Autowired
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, RoutingSlip> kafka) {
        this.orderRepository = orderRepository;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        Payload payload = slip.getPayload();

        Order order = get(payload.getId());

        if (payload.isInStock()) {
            order.setReady(true);
            order.setOutOfStock(false);
        } else {
            order.setReady(false);
            order.setOutOfStock(true);
        }

        orderRepository.save(order);
    }

    public long create(Order order) {
        orderRepository.save(order);

        long id = order.getId();

        Payload payload = new Payload(id, order.getPizza().getIngredients());
        RoutingSlip slip = new RoutingSlip(payload);

        log.log(INFO, format("Sent routing slip for order %d to topic", id));

        kafka.send(topic, slip);

        return id;
    }

    public Order get(long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

}
