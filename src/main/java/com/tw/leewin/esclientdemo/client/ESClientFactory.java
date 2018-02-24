package com.tw.leewin.esclientdemo.client;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

@Data
public class ESClientFactory {
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 9200;
    private static final String SCHEME = "http";
    private static final HttpHost HTTP_HOST = new HttpHost(HOST_NAME, PORT, SCHEME);
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 30000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 500;
    private static final int MAX_CONNECTION_NUM = 100;
    private static final int MAX_CONNECTION_PER_ROUTE = 100;
    private static RestClientBuilder builder;
    private static RestClient restClient;
    private static RestHighLevelClient restHighLevelClient;
    private static boolean uniqueConnectTimeConfig = false;
    private static boolean uniqueConnectNumConfig = true;


    static {
        init();
    }

    private static void init() {
        builder = RestClient.builder(HTTP_HOST);
        if(uniqueConnectTimeConfig){
            setConnectTimeOutConfig();
        }
        if(uniqueConnectNumConfig){
            setMultiConnectConfig();
        }
        restClient = builder.build();
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    private static void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
            return requestConfigBuilder;
        });
    }

    private static void setMultiConnectConfig() {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
           httpClientBuilder.setMaxConnTotal(MAX_CONNECTION_NUM);
           httpClientBuilder.setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE);
           return httpClientBuilder;
        });
    }

    public static RestClient getClient() {
        return restClient;
    }

    public static RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }

    public static void close() {
        if (restClient != null) {
            try {
                restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
