package com.demo.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.oauth2.kafkaProducer.OauthKafkaProducer;

@RestController
@RequestMapping(path = "/kafka")
public class KafkaController {

    @Autowired
    private OauthKafkaProducer oauthKafkaProducer;

    @GetMapping(value = "/get/{topic}")
    public ResponseEntity<?> callingKafka(@RequestParam(value = "msg", required = true) String msg,
                    @PathVariable(value = "topic", required = true) String topic) {
        oauthKafkaProducer.sendRecord(topic, msg);
        return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("msg",
                        "Successfully called kafka Producer and Consumer"));
    }
}
