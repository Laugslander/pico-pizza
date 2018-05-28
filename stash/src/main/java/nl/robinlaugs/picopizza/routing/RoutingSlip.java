package nl.robinlaugs.picopizza.routing;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

import static nl.robinlaugs.picopizza.routing.Action.TODO;

/**
 * @author Robin Laugs
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class RoutingSlip implements Serializable {

    @NonNull
    private Payload payload;

    private Action stockActionStatus = TODO;
    private Action ovenActionStatus = TODO;
    private Action stashActionStatus = TODO;

}
