package nl.robinlaugs.picopizza.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Robin Laugs
 */
@Entity(name = "t_order")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @NonNull
    private Pizza pizza;

    private boolean ready;

}
