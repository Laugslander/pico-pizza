package nl.robinlaugs.picopizza.stock.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author Robin Laugs
 */
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

}
