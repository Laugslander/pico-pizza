package nl.robinlaugs.picopizza.stash.data.redis;

import nl.robinlaugs.picopizza.stash.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Robin Laugs
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, String> {
}