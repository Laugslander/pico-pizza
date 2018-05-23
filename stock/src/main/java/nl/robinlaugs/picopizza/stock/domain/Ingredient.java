package nl.robinlaugs.picopizza.stock.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Robin Laugs
 */
@Entity(name = "t_ingredient")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    @NonNull
    private String name;

    @NonNull
    private int stock;

}
