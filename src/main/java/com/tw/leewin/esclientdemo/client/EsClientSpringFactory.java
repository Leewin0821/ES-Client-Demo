package com.tw.leewin.esclientdemo.client;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class EsClientSpringFactory {
  private static HttpHost HTTP_HOST;
  private static int CONNECT_TIMEOUT_MILLIS = 1000;
  private static int SOCKET_TIMEOUT_MILLIS = 30000;
  private static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
  private static int MAX_CONNECT_TOTAL = 30;
  private static int MAX_CONNECT_PER_ROUTE = 10;
  private static EsClientSpringFactory esClientSpringFactory = new EsClientSpringFactory();
  private RestClientBuilder builder;
  private RestClient restClient;
  private RestHighLevelClient restHighLevelClient;

  private EsClientSpringFactory() {
  }

  public static EsClientSpringFactory build(HttpHost httpHost, Integer maxConnectNum, Integer maxConnectPerRoute) {
    HTTP_HOST = httpHost;
    MAX_CONNECT_TOTAL = maxConnectNum;
    MAX_CONNECT_PER_ROUTE = maxConnectPerRoute;
    return esClientSpringFactory;
  }

  public static EsClientSpringFactory build(HttpHost httpHost, Integer connectTimeOut,
                                            Integer socketTimeOut, Integer connectionRequestTime,
                                            Integer maxConnectNum, Integer maxConnectPerRoute) {
    HTTP_HOST = httpHost;
    CONNECT_TIMEOUT_MILLIS = connectTimeOut;
    SOCKET_TIMEOUT_MILLIS = socketTimeOut;
    CONNECTION_REQUEST_TIMEOUT_MILLIS = connectionRequestTime;
    MAX_CONNECT_TOTAL = maxConnectNum;
    MAX_CONNECT_PER_ROUTE = maxConnectPerRoute;
    return esClientSpringFactory;
  }

  public void init() {
    builder = RestClient.builder(HTTP_HOST);
    setConnectTimeOutConfig();
    setMultiConnectConfig();
    restClient = builder.build();
    restHighLevelClient = new RestHighLevelClient(builder);
    System.out.println("init client factory!");
  }

  private void setMultiConnectConfig() {
    builder.setHttpClientConfigCallback(httpClientBuilder -> {
      httpClientBuilder.setMaxConnTotal(MAX_CONNECT_TOTAL);
      httpClientBuilder.setMaxConnPerRoute(MAX_CONNECT_PER_ROUTE);
      return httpClientBuilder;
    });
  }

  private void setConnectTimeOutConfig() {
    builder.setRequestConfigCallback(requestConfigBuilder -> {
      requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
      requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
      requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
      return requestConfigBuilder;
    });
  }

  public RestClient getRestClient() {
    return restClient;
  }

  public RestHighLevelClient getRestHighLevelClient() {
    return restHighLevelClient;
  }

  public void close() {
    if (restClient != null) {
      try {
        restClient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println("close client!");
  }
}
