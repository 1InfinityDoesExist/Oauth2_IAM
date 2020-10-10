package com.demo.oauth2.kafkaConsumer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OauthKafkaConsumer {

    public OauthKafkaConsumer() {
        log.info(":::::Inside OauthKafkaConsumer Constructor::::::");
    }

    @KafkaListener(topics = "bulk", containerFactory = "concurrentKafkaListenerContinerFactory")
    public void consumeRecords(ConsumerRecord<String, String> record) {
        log.info("::::::record {}", record.value());
    }

    /*
     * Extract Information like
     * 
     * Algorithm form token first part And other details like user_name, userId, scope and all from
     * second part of the token
     */
    @KafkaListener(topics = "token", containerFactory = "concurrentKafkaListenerContinerFactory")
    public void extractTokenInformation(ConsumerRecord<String, String> record) {
        log.info(":::::Inside extractTokenInformation method ::::::");
        String[] oauthTokenParts = record.value().split("\\.", 0);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] tokenBytes2 = decoder.decode(oauthTokenParts[1].getBytes(StandardCharsets.UTF_8));
        String jsonTokenString2 = new String(tokenBytes2);
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonTokenString2);
            for (Object object : jsonObject.keySet()) {
                String param = (String) object;
                log.info(":::::param {}", param);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        byte[] tokenBytes1 = decoder.decode(oauthTokenParts[0].getBytes(StandardCharsets.UTF_8));
        String jsonTokenString1 = new String(tokenBytes1);
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonTokenString1);
            for (Object obj : jsonObj.keySet()) {
                String key = (String) obj;
                log.info(":::::Algorithm {}", jsonObj.get(key));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
