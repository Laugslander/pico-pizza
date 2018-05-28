package nl.robinlaugs.picopizza.routing;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Robin Laugs
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Payload implements Serializable {

    @NonNull
    private long id;

    @NonNull
    private Collection<String> ingredients;

    private boolean inStock = false;
    private boolean isBaked = false;

}
