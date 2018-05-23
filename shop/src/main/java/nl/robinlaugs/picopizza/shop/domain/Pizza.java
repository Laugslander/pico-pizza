package nl.robinlaugs.picopizza.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Pizza implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private long id;

    @ElementCollection
    @NonNull
    private Collection<String> ingredients;

}
