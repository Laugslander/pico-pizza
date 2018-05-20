package nl.robinlaugs.picopizza.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Robin Laugs
 */
@Entity(name = "t_pizza")
public class Pizza implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    @ElementCollection
    private Collection<String> ingredients;

    public Pizza(Collection<String> ingredients) {
        this.ingredients = ingredients;
    }

    private Pizza() {
        // JPA
    }

    public long getId() {
        return id;
    }

    public Collection<String> getIngredients() {
        return ingredients;
    }

}
