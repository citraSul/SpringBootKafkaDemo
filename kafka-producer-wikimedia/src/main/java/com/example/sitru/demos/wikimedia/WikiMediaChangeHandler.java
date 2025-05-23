package com.example.sitru.demos.wikimedia;


import com.launchdarkly.eventsource.EventHandler;

import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class WikiMediaChangeHandler implements EventHandler {

    KafkaProducer<String,String> kafkaProducer;
     String topic;
    private final Logger LOGGER = LoggerFactory.getLogger(WikiMediaChangeHandler.class.getSimpleName());

    public WikiMediaChangeHandler(KafkaProducer<String,String> kafkaProducer,String topic){
        this.kafkaProducer= kafkaProducer;
        this.topic= topic;

    }
    @Override
    public void onOpen()  {

    }

    @Override
    public void onClosed()  {

    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent)  {
        LOGGER.info("-----------------"+messageEvent.getData());
         kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));

    }

    @Override
    public void onComment(String s)  {

    }

    @Override
    public void onError(Throwable t) {
        LOGGER.error("Error in streaming",t);

    }
}
