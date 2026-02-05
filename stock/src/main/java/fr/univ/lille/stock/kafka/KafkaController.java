package fr.univ.lille.stock.kafka;

import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/messages")
    public void produceMessage(@RequestBody String message) {
        kafkaProducer.produce(message);
    }
}