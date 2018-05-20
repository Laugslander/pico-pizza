package nl.robinlaugs.picopizza.stock.data;

import nl.robinlaugs.picopizza.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Robin Laugs
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
