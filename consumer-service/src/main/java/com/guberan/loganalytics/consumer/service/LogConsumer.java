package com.guberan.loganalytics.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.loganalytics.consumer.model.LogMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LogConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "logs-dev", groupId = "log-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            LogMessage log = objectMapper.readValue(record.value(), LogMessage.class);
            System.out.printf("🔍 Received log [%s] %s - %s: %s%n",
                    log.getTimestamp(), log.getService(), log.getLevel(), log.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Failed to parse log: " + record.value());
            e.printStackTrace();
        }
    }
}