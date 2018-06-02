package nl.robinlaugs.picopizza.shop.data;

import nl.robinlaugs.picopizza.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Robin Laugs
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
