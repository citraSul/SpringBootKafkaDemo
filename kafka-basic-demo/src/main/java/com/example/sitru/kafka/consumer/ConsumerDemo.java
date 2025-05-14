package com.example.sitru.kafka.consumer;

import com.example.sitru.kafka.producer.ProducerDemoKeys;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerDemo.class);


    public static void main(String[] args) {
        LOGGER.info("I am a kafka Consumer ");

        String groupId ="my-java-application";
        String topic="demo.java";

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");


        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer",StringDeserializer.class.getName());

        properties.setProperty("group.id",groupId);
        properties.setProperty("auto.offset.reset","earliest");


        //Create Consumer
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);

        //get a reference the current thread
        final Thread mainThread =Thread.currentThread();


            //subscribe the topic
        consumer.subscribe(Arrays.asList(topic));

            //poll the data
        while (true) {
            LOGGER.info("polling");
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String ,String> record : consumerRecords) {
                LOGGER.info("Key: " + record.key() + ", Value: " + record.value());
                LOGGER.info("Partition: " + record.partition() + ", Offset: " + record.offset());
                }
            }
    }
}
