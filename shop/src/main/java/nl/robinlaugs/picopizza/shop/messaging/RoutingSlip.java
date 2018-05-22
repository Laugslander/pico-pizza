package nl.robinlaugs.picopizza.shop.messaging;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Robin Laugs
 */
public class RoutingSlip implements Serializable {

    private Collection<String> ingredients = new LinkedList<>();

    public Collection<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<String> ingredients) {
        this.ingredients = ingredients;
    }

}
