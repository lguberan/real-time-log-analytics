package com.guberan.loganalytics.consumer.repository;

import com.guberan.loganalytics.consumer.model.LogMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends ElasticsearchRepository<LogMessage, String> {
}