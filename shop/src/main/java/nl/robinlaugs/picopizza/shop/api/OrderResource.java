package nl.robinlaugs.picopizza.shop.api;


import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Robin Laugs
 */
@RestController
@RequestMapping("/order")
public class OrderResource {

    private final OrderService orderService;

    @Autowired
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable long id) {
        return orderService.get(id);
    }

    @PostMapping
    public long post(@RequestBody Order order) {
        return orderService.create(order);
    }

}
