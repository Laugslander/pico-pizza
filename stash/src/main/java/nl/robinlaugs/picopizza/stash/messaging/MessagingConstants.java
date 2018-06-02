package nl.robinlaugs.picopizza.stash.messaging;

import static java.lang.String.format;

/**
 * @author Robin Laugs
 */
public final class MessagingConstants {

    /**
     * Topics
     */

    public static final String ROUTING_TOPIC = "routing";

    private MessagingConstants() {
        throw new UnsupportedOperationException(format("%s should not be instantiated",
                MessagingConstants.class.getSimpleName()));
    }

}
