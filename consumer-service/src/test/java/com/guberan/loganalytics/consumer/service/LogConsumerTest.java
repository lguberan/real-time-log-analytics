package com.guberan.loganalytics.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.loganalytics.consumer.model.LogMessage;
import com.guberan.loganalytics.consumer.repository.LogRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogConsumerTest {

    private LogConsumer logConsumer;
    private LogRepository mockRepository;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockRepository = mock(LogRepository.class);
        logConsumer = new LogConsumer(mockRepository);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testConsume_ValidLogMessage_SavesToRepository() throws Exception {
        // given
        LogMessage inputLog = new LogMessage(null, "2025-04-15T10:00:00Z", "test-service", "INFO", "unit test log");
        String json = objectMapper.writeValueAsString(inputLog);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("logs-dev", 0, 0, null, json);

        // when
        logConsumer.consume(record);

        // then
        ArgumentCaptor<LogMessage> captor = ArgumentCaptor.forClass(LogMessage.class);
        verify(mockRepository, times(1)).save(captor.capture());

        LogMessage savedLog = captor.getValue();
        assertNotNull(savedLog.getId());
        assertEquals("unit test log", savedLog.getMessage());
        assertEquals("test-service", savedLog.getService());
    }

    @Test
    void testConsume_InvalidJson_DoesNotThrow() {
        // given
        String invalidJson = "{not_valid}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("logs-dev", 0, 0, null, invalidJson);

        // when / then
        assertDoesNotThrow(() -> logConsumer.consume(record));
        verifyNoInteractions(mockRepository); // nothing should be saved
    }

}