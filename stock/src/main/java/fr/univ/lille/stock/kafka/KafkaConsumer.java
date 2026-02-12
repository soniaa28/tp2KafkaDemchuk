package fr.univ.lille.stock.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univ.lille.stock.service.StockService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private final StockService stockService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaConsumer(StockService stockService) {
        this.stockService = stockService;
    }

    @KafkaListener(topics = "orders", groupId = "stock-group")
    public void consume(String message) {
        try {
            OrderSubmittedEvent event = objectMapper.readValue(message, OrderSubmittedEvent.class);
            stockService.applyOrder(event);
        } catch (Exception e) {
            LOGGER.error("Erreur de désérialisation: {}", message, e);
        }
    }
}