package com.example.sitru.springboot_Kafka_demo.kafka;

import com.example.sitru.springboot_Kafka_demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);
    @KafkaListener(topics = "myTopicOne_json",groupId = "myGroup")
    public void  jsonPublisher(User user){
    LOGGER.info(String.format("Json Message received -> %s", user.toString()));

    }
}
