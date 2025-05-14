package com.example.sitru.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallBack {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);



    public static void main(String[] args) {
        LOGGER.info("I am a kafka producer demo with call back");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());

        //properties.setProperty("batch.size","400");

        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        for (int  j= 0; j <10 ; j++) {
            for (int i = 0; i <30 ; i++) {
                ProducerRecord<String ,String> producerRecord = new ProducerRecord<>("demo_topic","hello world"+i);
                //send the data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        //executed every time successfully sent or when exception is thrown
                        if(e== null){
                            LOGGER.info("Received new metadata \n"+
                                    "Topics: "+ recordMetadata.topic()+"\n"+
                                    "Partition: "+ recordMetadata.partition()+"\n"+
                                    "Offset: "+ recordMetadata.offset()+"\n"+
                                    "TimeStamp: "+ recordMetadata.timestamp());
                        }else {
                            LOGGER.error("Error while producing", e);

                        }
                    }
                });
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



        // tell the producer to send all the data and block until it is done
        producer.flush();
        //flush and close the producer
        producer.close();
    }
}
