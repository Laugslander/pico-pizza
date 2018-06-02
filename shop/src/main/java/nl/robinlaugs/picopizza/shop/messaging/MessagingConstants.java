package nl.robinlaugs.picopizza.shop.messaging;

import static java.lang.String.format;

/**
 * @author Robin Laugs
 */
public final class MessagingConstants {

    /**
     * Topics
     */

    public static final String ROUTING_TOPIC = "routing";
    public static final String SHOP_STASH_TOPIC = "shop_stash";
    public static final String STASH_SHOP_TOPIC = "stash_shop";

    private MessagingConstants() {
        throw new UnsupportedOperationException(format("%s should not be instantiated",
                MessagingConstants.class.getSimpleName()));
    }

}
