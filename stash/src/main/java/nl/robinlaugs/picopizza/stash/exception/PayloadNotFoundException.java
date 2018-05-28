package nl.robinlaugs.picopizza.stash.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Robin Laugs
 */
@ResponseStatus(NOT_FOUND)
public class PayloadNotFoundException extends RuntimeException {

    public PayloadNotFoundException(String id) {
        super(format("Could not find payload with id %s", id));
    }

}
