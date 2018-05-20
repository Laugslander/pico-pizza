package nl.robinlaugs.picopizza.shop.api;


import nl.robinlaugs.picopizza.shop.data.OrderRepository;
import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author Robin Laugs
 */
@RestController
@RequestMapping("/order")
public class OrderResource {

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, String> kafka;

    @Autowired
    public OrderResource(OrderRepository orderRepository, KafkaTemplate<String, String> kafka) {
        this.orderRepository = orderRepository;
        this.kafka = kafka;
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @PostMapping
    public long post(@RequestBody Order order) {
        orderRepository.save(order);

        kafka.send("shop_stock", "id", "test");

        return order.getId();
    }

}
