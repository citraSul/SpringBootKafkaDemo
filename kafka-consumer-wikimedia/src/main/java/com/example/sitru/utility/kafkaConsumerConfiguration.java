package com.example.sitru.utility;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class kafkaConsumerConfiguration {

    public static KafkaConsumer<String,String> createKafkaConsumer(){


        String groupId ="my-java-application";
        String topic="consumer-opensearch-demo";

        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers","localhost:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer",StringDeserializer.class.getName());
        properties.setProperty("group.id",groupId);
        properties.setProperty("auto.offset.reset","earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");


        return new KafkaConsumer<>(properties);
    }
}
