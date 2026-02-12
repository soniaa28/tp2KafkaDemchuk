package fr.univ.lille.stock.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderSubmitted(Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("my-first-topic", json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}