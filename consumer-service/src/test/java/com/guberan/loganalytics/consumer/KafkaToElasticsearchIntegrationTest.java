package com.guberan.loganalytics.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guberan.loganalytics.consumer.model.LogMessage;
import com.guberan.loganalytics.consumer.repository.LogRepository;
import org.apache.kafka.common.serialization.StringSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"logs-dev"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@Testcontainers
public class KafkaToElasticsearchIntegrationTest {

    @Container
    static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.12.1")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("discovery.type", "single-node");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @DynamicPropertySource
    static void elasticsearchProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearch::getHttpHostAddress);
    }

    @AfterEach
    void cleanUpTestLogs() {
        logRepository.deleteAll();
    }

    @Test
    void testKafkaMessageIsIndexedInElasticsearch() throws Exception {
        String uniqueMessage = "full-kafka-es-test-" + UUID.randomUUID();

        LogMessage log = LogMessage.builder()
                .timestamp(Instant.now().toString())
                .service("test-service")
                .level("INFO")
                .message(uniqueMessage)
                .build();

        Map<String, Object> props = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new StringSerializer()));
        kafkaTemplate.send("logs-dev", objectMapper.writeValueAsString(log)).get();

        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    Iterable<LogMessage> results = logRepository.findAll();
                    assertThat(results).anyMatch(l -> uniqueMessage.equals(l.getMessage()));
                });
    }
}


