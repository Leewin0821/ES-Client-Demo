package com.tw.leewin.esclientdemo.config;

import com.tw.leewin.esclientdemo.client.EsClientSpringFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = EsClientSpringFactory.class)
@PropertySource(value = {"classpath:elasticSearch.properties"})
public class EsConfig {

  @Value("${httpHost.host}")
  private String host;

  @Value("${httpHost.port}")
  private int port;

  @Value("${httpHost.scheme}")
  private String scheme;

  @Value("${esClient.connectNum}")
  private Integer connectNum;

  @Value("${esClient.connectPerRoute}")
  private Integer connectPerRoute;

  @Bean
  public HttpHost httpHost() {
    return new HttpHost(host, port, scheme);
  }

  @Bean(initMethod = "init", destroyMethod = "close")
  public EsClientSpringFactory getFactory() {
    return EsClientSpringFactory.build(httpHost(), connectNum, connectPerRoute);
  }

  @Bean
  @Scope("singleton")
  public RestClient getRestClient() {
    return getFactory().getRestClient();
  }

  @Bean
  @Scope("singleton")
  public RestHighLevelClient getRestHighLevelClient() {
    return getFactory().getRestHighLevelClient();
  }

}
