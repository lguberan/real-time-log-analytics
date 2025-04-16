package com.guberan.loganalytics.producer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@AutoConfigureMockMvc
class LogProducerTest {

    @Autowired
    private LogProducer logProducer;

    @Test
    void testSendLog() {
        assertDoesNotThrow(() -> logProducer.sendLog());
    }
}