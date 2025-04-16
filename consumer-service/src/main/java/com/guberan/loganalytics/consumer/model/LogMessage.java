package com.guberan.loganalytics.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage {
    private String timestamp;
    private String service;
    private String level;
    private String message;
}