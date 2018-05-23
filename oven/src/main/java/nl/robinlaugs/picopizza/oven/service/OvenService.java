package nl.robinlaugs.picopizza.oven.service;

import lombok.extern.java.Log;
import nl.robinlaugs.picopizza.routing.RoutingSlip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static nl.robinlaugs.picopizza.routing.Action.DONE;

/**
 * @author Robin Laugs
 */
@Service
@Log
public class OvenService {

    @Value("${kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, RoutingSlip> kafkaTemplate;

    @Autowired
    public OvenService(KafkaTemplate<String, RoutingSlip> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory")
    private void listen(RoutingSlip slip) {
        log.log(INFO, format("Received routing slip for order %d", slip.getPayload().getId()));

        bake(slip);

        kafkaTemplate.send(topic, slip);
    }

    private void bake(RoutingSlip slip) {
        try {
            sleep(3_000);
        } catch (InterruptedException e) {
            log.log(SEVERE, "An error occurred while baking", e);
        }

        slip.getPayload().setBaked(true);
        slip.setOven(DONE);
    }

}
