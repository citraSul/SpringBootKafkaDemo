package com.example.sitru.kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemoKeys.class);



    public static void main(String[] args) {

        LOGGER.info("I am a kafka producer with keys");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());


        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
for(int j =0; j<2;j++) {
    for (int i = 0; i < 10; i++) {
        String topic = "demo.java";
        String key = "id_" + i;
        String value = "Hello sitru " + i;
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        //send the data
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                //executed every time successfully sent or when exception is thrown
                if (e == null) {
                    LOGGER.info(
                            "Key: " + key + " | Partition: " + recordMetadata.partition());
                } else {
                    LOGGER.error("Error while producing", e);

                }
            }
        });
    }
}



        // tell the producer to send all the data and block until it is done
        producer.flush();
        //flush and close the producer
        producer.close();
    }
}
