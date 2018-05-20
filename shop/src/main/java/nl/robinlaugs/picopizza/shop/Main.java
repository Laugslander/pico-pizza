package nl.robinlaugs.picopizza.shop;

import nl.robinlaugs.picopizza.shop.data.OrderRepository;
import nl.robinlaugs.picopizza.shop.domain.Order;
import nl.robinlaugs.picopizza.shop.domain.Pizza;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.asList;
import static org.springframework.boot.SpringApplication.run;

/**
 * @author Robin Laugs
 */
@SpringBootApplication(scanBasePackages= "nl.robinlaugs.picopizza")
public class Main {

    public static void main(String[] args) {
        run(Main.class, args);
    }

    @Bean
    CommandLineRunner init(OrderRepository orderRepository) {
        return (evt) -> {
            Pizza pizza = new Pizza(asList("chicken", "cheese"));
            Order order = new Order(pizza);

            orderRepository.save(order);
        };
    }

}