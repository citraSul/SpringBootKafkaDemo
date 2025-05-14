package com.example.sitru.demos.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class WikiMediaChangesProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WikiMediaChangesProducer.class);

    public static void main(String[] args) {
        LOGGER.info("I am a kafka WikiMedia producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","localhost:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer",StringSerializer.class.getName());

        //create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikiMedia.recentchange";
        String url= "https://stream.wikimedia.org/v2/stream/recentchange";

        EventHandler eventHandler = new WikiMediaChangeHandler(producer,topic);

        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource =builder.build();

        //start the event
        eventSource.start();
        //produce for 10 minute block the program
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
