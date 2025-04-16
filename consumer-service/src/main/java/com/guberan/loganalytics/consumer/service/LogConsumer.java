package com.guberan.loganalytics.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.loganalytics.consumer.model.LogMessage;
import com.guberan.loganalytics.consumer.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class LogConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LogRepository logRepository;

    public LogConsumer(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @KafkaListener(topics = "logs-dev", groupId = "log-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            LogMessage logMsg = objectMapper.readValue(record.value(), LogMessage.class);
            logMsg.setId(UUID.randomUUID().toString());

            logRepository.save(logMsg);
            log.info("✅ Stored log: {}", logMsg);
        } catch (Exception e) {
            log.error("❌ Failed to process message: {}", record.value(), e);
        }
    }
}