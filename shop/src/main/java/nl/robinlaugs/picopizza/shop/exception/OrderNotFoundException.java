package nl.robinlaugs.picopizza.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;

/**
 * @author Robin Laugs
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(long id) {
        super(format("Could not find order with id %d", id));
    }

}
