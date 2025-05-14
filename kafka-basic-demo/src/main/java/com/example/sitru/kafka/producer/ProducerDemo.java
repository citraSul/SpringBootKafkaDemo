package com.example.sitru.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemo.class);



    public static void main(String[] args) {
        LOGGER.info("I am a kafka producer");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //create a producer Record
        ProducerRecord<String ,String> producerRecord = new ProducerRecord<>("demo_topic","hello world");
        //send the data
        producer.send(producerRecord);
        // tell the producer to send all the data and block until it is done
        producer.flush();
        //flush and close the producer
        producer.close();
    }



}
