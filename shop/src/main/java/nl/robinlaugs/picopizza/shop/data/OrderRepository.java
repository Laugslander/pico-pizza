package nl.robinlaugs.picopizza.shop.data;

import nl.robinlaugs.picopizza.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Robin Laugs
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
