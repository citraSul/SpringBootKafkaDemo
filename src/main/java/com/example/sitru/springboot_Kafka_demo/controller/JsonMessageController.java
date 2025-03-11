package com.example.sitru.springboot_Kafka_demo.controller;

import com.example.sitru.springboot_Kafka_demo.kafka.JsonKafkaProducer;
import com.example.sitru.springboot_Kafka_demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/kafka")
public class JsonMessageController {

    private final JsonKafkaProducer jsonKafkaProducer;

    public JsonMessageController(JsonKafkaProducer jsonKafkaProducer) {
        this.jsonKafkaProducer = jsonKafkaProducer;
    }
    @PostMapping("/publisher")
    public ResponseEntity<String> jsonPublisher(@RequestBody User user){
        jsonKafkaProducer.sendJsonMessage(user);
        return ResponseEntity.ok("Json Message sent to Kafka Topic");

    }
}
