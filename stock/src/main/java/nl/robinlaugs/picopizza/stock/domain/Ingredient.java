package nl.robinlaugs.picopizza.stock.domain;

import lombok.*;

import javax.persistence.Column;
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
public class Ingredient extends BaseEntity {

    @Column(unique = true)
    private String name;

}
