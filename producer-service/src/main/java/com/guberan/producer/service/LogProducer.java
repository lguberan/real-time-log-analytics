package com.guberan.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.producer.model.LogMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class LogProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String[] levels = {"INFO", "WARN", "ERROR"};
    private final Random random = new Random();

    public LogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void sendLog() throws JsonProcessingException {
        LogMessage log = new LogMessage(
                Instant.now().toString(),
                "auth-service",
                levels[random.nextInt(levels.length)],
                "Sample log message #" + random.nextInt(1000)
        );

        String json = objectMapper.writeValueAsString(log);
        kafkaTemplate.send("logs-dev", json);
        System.out.println("Sent log: " + json);
    }
}