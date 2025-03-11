package com.example.sitru.springboot_Kafka_demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic myTopics(){
        return TopicBuilder.name("myTopicOne").build();

    }
    @Bean
    public NewTopic myJsonTopics(){
        return TopicBuilder.name("myTopicOne_json").build();

    }
}
