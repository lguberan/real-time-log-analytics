# Real-Time Log Analytics Platform

This project demonstrates a scalable architecture for real-time log ingestion, processing, and analysis using Apache
Kafka and Spring Boot.

## 🧱 Architecture Overview

![Architecture Diagram](docs/architecture.png)

- **Log Producer (Spring Boot)**: Simulates application logs and sends them to Kafka.
- **Kafka Broker**: Manages log topics and message streaming.
- **Log Consumer (Spring Boot)**: Reads logs from Kafka for further processing or storage.
- **Optional**: Integration with **Elasticsearch** and **Kibana** for visualization and search.

## 🔧 Technologies

- Java 17 + Spring Boot 3.x
- Apache Kafka
- Docker + Docker Compose
- (Optional) Elasticsearch + Kibana
- Maven / Lombok / Logback

## 📦 Modules

- `producer-service`: Simulates microservices logging into Kafka
- `consumer-service`: Consumes and processes Kafka log messages
- `docker-compose.yml`: Local Kafka + Zookeeper setup (optionally with ELK stack)

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/lguberan/real-time-log-analytics.git
   cd real-time-log-analytics