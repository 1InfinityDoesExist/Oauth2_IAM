package com.demo.oauth2.kafkaConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OauthKafkaConsumer {

    public OauthKafkaConsumer() {
        log.info(":::::Inside OauthKafkaConsumer Constructor::::::");
    }

    @KafkaListener(topics = "token", containerFactory = "concurrentKafkaListenerConatinerFactory")
    public void consumeRecords(ConsumerRecord<String, String> record) {
        log.info("::::::record {}", record.value());
    }
}
