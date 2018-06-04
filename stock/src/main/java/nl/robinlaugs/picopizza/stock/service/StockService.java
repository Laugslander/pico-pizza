package nl.robinlaugs.picopizza.stock.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.Payload;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import nl.robinlaugs.picopizza.stock.data.IngredientRepository;
import nl.robinlaugs.picopizza.stock.domain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.logging.Level.INFO;
import static nl.robinlaugs.picopizza.routing.Action.CONTINUE;
import static nl.robinlaugs.picopizza.routing.Action.STOP;
import static nl.robinlaugs.picopizza.stock.messaging.MessagingConstants.ROUTING_TOPIC;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class StockService {

    private final IngredientRepository ingredientRepository;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    @Autowired
    public StockService(IngredientRepository ingredientRepository, KafkaTemplate<String, RoutingSlip> kafka) {
        this.ingredientRepository = ingredientRepository;
        this.kafka = kafka;
    }

    @KafkaListener(topics = ROUTING_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
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

        Payload payload = slip.getPayload();

        log.log(INFO, format("Ingredients %s are %s stock", ingredients, payload.isInStock() ? "in" : "out of"));

        kafka.send(ROUTING_TOPIC, slip);

        log.log(INFO, format("Sent routing slip for order %d to topic", payload.getId()));
    }

    private boolean checkStock(Collection<String> ingredients) {
        boolean inStock = true;

        log.log(INFO, "Checking stock status...");

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

            log.log(INFO, format("Decreased %s stock to %d", ingredient.getName(), ingredient.getStock()));
        }
    }

    public void createAll(Collection<Ingredient> ingredients) {
        ingredientRepository.saveAll(ingredients);
    }

}
