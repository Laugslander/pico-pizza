package nl.robinlaugs.picopizza.shop.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Robin Laugs
 */
@ResponseStatus(NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(long id) {
        super(format("Could not find order with id %d", id));
    }

}
