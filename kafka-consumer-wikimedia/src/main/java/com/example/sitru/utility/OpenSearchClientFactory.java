package com.example.sitru.utility;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

import java.net.URI;

public class OpenSearchClientFactory {

    public static RestHighLevelClient createOpenSearchClient() {
        String connString = "http://localhost:9200";
        URI connUri = URI.create(connString);

        String userInfo = connUri.getUserInfo();
        HttpHost host = new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme());

        if (userInfo == null) {
            return new RestHighLevelClient(
                    RestClient.builder(host)
            );
        } else {
            String[] auth = userInfo.split(":");
            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            return new RestHighLevelClient(
                    RestClient.builder(host)
                            .setHttpClientConfigCallback(httpClientBuilder ->
                                    httpClientBuilder
                                            .setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()))
            );
        }
    }
}
