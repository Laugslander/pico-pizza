package nl.robinlaugs.picopizza.stock.exception;

import static java.lang.String.format;

/**
 * @author Robin Laugs
 */
public class IngredientOutOfStockException extends Exception {

    public IngredientOutOfStockException(String name) {
        super(format("Ingredient %s out of stock", name));
    }

}
