package nl.robinlaugs.picopizza.shop.api;


import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Robin Laugs
 */
@RestController
@RequestMapping("/order")
public class OrderResource {

    private final ShopService shopService;

    @Autowired
    public OrderResource(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable long id) {
        return shopService.get(id);
    }

    @PostMapping
    public long post(@RequestBody Order order) {
        return shopService.create(order);
    }

}
