package nl.robinlaugs.picopizza.stock.data;

import nl.robinlaugs.picopizza.stock.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Robin Laugs
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Ingredient findByName(String name);

}
