package nl.robinlaugs.picopizza.stock.domain;

import lombok.*;

import javax.persistence.Entity;

/**
 * @author Robin Laugs
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Stock extends BaseEntity {

    private Ingredient ingredient;

    private int stock;

    public void increase() {
        stock--;
    }

    public void decrease() {
        stock++;
    }

}
