package nl.robinlaugs.picopizza.oven.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static nl.robinlaugs.picopizza.oven.messaging.MessagingConstants.ROUTING_TOPIC;
import static nl.robinlaugs.picopizza.routing.Action.CONTINUE;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class OvenService {

    private static final int BAKING_TIME_SECONDS = 10;

    private final KafkaTemplate<String, RoutingSlip> kafka;

    @Autowired
    public OvenService(KafkaTemplate<String, RoutingSlip> kafka) {
        this.kafka = kafka;
    }

    @KafkaListener(topics = ROUTING_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        long id = slip.getPayload().getId();

        log.log(INFO, format("Received routing slip for order %d", id));

        bake(slip);

        kafka.send(ROUTING_TOPIC, slip);

        log.log(INFO, format("Sent routing slip for order %d to topic", id));
    }

    private void bake(RoutingSlip slip) {
        try {
            long id = slip.getPayload().getId();

            log.log(INFO, format("Started baking order with id %d", id));

            sleep(BAKING_TIME_SECONDS * 1000);

            log.log(INFO, format("Finished baking order with id %d", id));
        } catch (InterruptedException e) {
            log.log(SEVERE, "An error occurred while baking", e);
        }

        slip.getPayload().setBaked(true);
        slip.setOvenActionStatus(CONTINUE);
    }

}
