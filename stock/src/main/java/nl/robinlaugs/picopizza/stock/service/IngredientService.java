package nl.robinlaugs.picopizza.stock.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.stock.data.IngredientRepository;
import nl.robinlaugs.picopizza.stock.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.routing.Action.CONTINUE;
import static nl.robinlaugs.picopizza.routing.Action.STOP;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class IngredientService {

    @Value("${kafka.topic}")
    private String topic;

    private final IngredientRepository ingredientRepository;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, KafkaTemplate<String, RoutingSlip> kafka) {
        this.ingredientRepository = ingredientRepository;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        Collection<String> ingredients = slip.getPayload().getIngredients();

        if (checkStock(ingredients)) {
            slip.getPayload().setInStock(true);
            slip.setStockActionStatus(CONTINUE);

            decreaseStock(ingredients);
        } else {
            slip.setStockActionStatus(STOP);
        }


        kafka.send("routing-topic", slip);
    }

    private boolean checkStock(Collection<String> ingredients) {
        boolean inStock = true;

        for (String i : ingredients) {
            Ingredient ingredient = ingredientRepository.findByName(i);

            if (isNull(ingredient) || ingredient.getStock() <= 0) {
                inStock = false;
                break;
            }
        }

        return inStock;
    }

    private void decreaseStock(Collection<String> ingredients) {
        for (String i : ingredients) {
            Ingredient ingredient = ingredientRepository.findByName(i);

            int stock = ingredient.getStock();

            ingredient.setStock(stock - 1);
        }
    }

    public void createAll(Collection<Ingredient> ingredients) {
        ingredientRepository.saveAll(ingredients);
    }

}
