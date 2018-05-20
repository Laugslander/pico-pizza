package nl.robinlaugs.picopizza.stock;

import nl.robinlaugs.picopizza.stock.data.IngredientRepository;
import nl.robinlaugs.picopizza.stock.domain.Ingredient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Collection;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.springframework.boot.SpringApplication.run;

/**
 * @author Robin Laugs
 */
@SpringBootApplication
public class Main {

    private static final Logger logger = getLogger(Main.class.getName());

    @KafkaListener(topics = "shop_stock", groupId = "1")
    private void listen(String message) {
        System.out.println(message);
        logger.log(INFO, message);
    }

    public static void main(String[] args) {
        run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(IngredientRepository ingredientRepository) {
        return (args) -> {
            Ingredient ingredient = Ingredient.builder().name("Mozzarella").build();

            ingredientRepository.save(ingredient);

            Collection<Ingredient> ingredients = ingredientRepository.findAll();

            ingredients.forEach(System.out::println);
        };
    }

}
