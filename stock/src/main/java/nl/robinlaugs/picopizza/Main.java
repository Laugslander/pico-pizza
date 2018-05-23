package nl.robinlaugs.picopizza;

import nl.robinlaugs.picopizza.stock.domain.Ingredient;
import nl.robinlaugs.picopizza.stock.service.IngredientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.springframework.boot.SpringApplication.run;

/**
 * @author Robin Laugs
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(IngredientService ingredientService) {
        return (args) -> {
            Collection<Ingredient> ingredients = asList(
                    new Ingredient("cheese", 5),
                    new Ingredient("tomato", 2),
                    new Ingredient("chicken", 3),
                    new Ingredient("beef", 4),
                    new Ingredient("mushrooms", 3),
                    new Ingredient("tuna", 2),
                    new Ingredient("pepperoni", 4)
            );

            ingredientService.createAll(ingredients);
        };
    }

}
