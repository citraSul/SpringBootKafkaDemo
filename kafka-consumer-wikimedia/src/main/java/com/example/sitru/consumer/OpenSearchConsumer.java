package com.example.sitru.consumer;

import com.example.sitru.utility.OpenSearchClientFactory;
import com.example.sitru.utility.kafkaConsumerConfiguration;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

public class OpenSearchConsumer {


    public static void main(String[] args) throws IOException {
       final Logger LOGGER =LoggerFactory.getLogger(OpenSearchConsumer.class.getSimpleName());

        //creating open search
        RestHighLevelClient openSearchClient = OpenSearchClientFactory.createOpenSearchClient();

        KafkaConsumer<String,String> consumer = kafkaConsumerConfiguration.createKafkaConsumer();

        try(openSearchClient; consumer) {

            boolean indexExists = openSearchClient.indices().exists( new GetIndexRequest("wikimedia"),RequestOptions.DEFAULT);

           if(!indexExists){

            CreateIndexRequest createIndexRequest = new CreateIndexRequest("wikiMedia.recentchange");
            openSearchClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            LOGGER.info("the wikimedia index has been created");

           }else{
               LOGGER.info("the wikimedia index already exists");
           }
           consumer.subscribe(Collections.singleton("wikiMedia.recentchange"));

           while(true){
               ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(3000));

               int recordCount = records.count();
               LOGGER.info("Received " + recordCount + " records");

               BulkRequest bulkRequest = new BulkRequest();

               for(ConsumerRecord<String,String> record: records){
                  //strategy one
                   //String id = record.topic() +"_"+record.partition()+"_"+record.offset();
                   try {
                       //strategy two
                       String id = extractId(record.value());
                       IndexRequest indexRequest = new IndexRequest("wikimedia")
                               .source(record.value(), XContentType.JSON)
                               .id(id);

                       //IndexResponse indexResponse = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);
                       bulkRequest.add(indexRequest);


                      // LOGGER.info("Inserted 1 document into opensearch{}", indexResponse.getId());
                   } catch (Exception e){

                   }

               }
               if(bulkRequest.numberOfActions() >0) {
                   BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                   LOGGER.info("Inserted "+bulkResponse.getItems().length + " records(.)");
               }

               try{
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               //commit offset after the batch is consumed
               consumer.commitSync();
               LOGGER.info("offset have been commited");
           }


        }



   }

    private static String extractId(String value) {
        return JsonParser.parseString(value)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();

    }
}
