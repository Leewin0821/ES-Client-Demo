package com.tw.leewin.esclientdemo.client;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

@Data
public class ESClient {
    private static ESClient ourInstance = new ESClient();

    public static ESClient getInstance() {
        return ourInstance;
    }

    private RestHighLevelClient client;

    private ESClient() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")
        ));
    }

    public void closeClient() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
