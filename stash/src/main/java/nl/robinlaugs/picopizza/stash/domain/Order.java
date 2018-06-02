package nl.robinlaugs.picopizza.stash.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Robin Laugs
 */
@RedisHash(value = "order")
@Data
@AllArgsConstructor
public class Order implements Serializable {

    @Id
    String id;

}
