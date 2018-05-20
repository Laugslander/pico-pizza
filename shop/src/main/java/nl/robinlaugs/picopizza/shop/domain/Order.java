package nl.robinlaugs.picopizza.shop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Robin Laugs
 */
@Entity(name = "t_order")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private Pizza pizza;

    public Order(Pizza pizza) {
        this.pizza = pizza;
    }

    private Order() {
        // JPA
    }

    public long getId() {
        return id;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

}
