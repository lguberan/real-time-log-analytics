package com.guberan.loganalytics.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "logs")
public class LogMessage {

    @Id
    private String id;

    private String timestamp;
    private String service;
    private String level;
    private String message;
}