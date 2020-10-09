package com.demo.oauth2.kafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OauthKafkaProducer {

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    public OauthKafkaProducer() {
        log.info(":::::::::::::Inside OauthKafkaProducer Config:::::::");
    }

    public ListenableFuture<SendResult<Integer, String>> sendRecord(String topic, String msg) {
        log.info("::::::Inside OauthKafkaProducer Class, sendRecord method::::");
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, msg);
        log.info(":::::futrue {}", future.isDone());
        return future;
    }
}
