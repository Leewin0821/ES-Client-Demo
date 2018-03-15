package com.tw.leewin.esclientdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.tw.leewin.esclientdemo.config.EsConfig;
import com.tw.leewin.esclientdemo.domain.News;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableConfigurationProperties
@SpringBootTest(classes = {EsConfig.class})
public class ClientTest {

  private String index;
  private String type;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @Before
  public void setUp() throws Exception {
    index = "demo";
    type = "demo";
  }

  @Test
  public void testAdd() {
    IndexRequest indexRequest = new IndexRequest(index, type);
    News news = new News();
    news.setTitle("This is title one");
    news.setTag("sport");
    news.setPublishTime("2018-01-24T23:59:30Z");
    ObjectMapper mapper = new ObjectMapper();
    String source = StringUtils.EMPTY;
    try {
      source = mapper.writeValueAsString(news);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    indexRequest.source(source, XContentType.JSON);
    try {
      restHighLevelClient.index(indexRequest);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUpdate() {
    String id = "lwe4GGIBpEuIUUQvUz3R";
    UpdateRequest updateRequest = new UpdateRequest(index, type, id);
    updateRequest.doc(ImmutableMap.of("tag", "swim"));
    try {
      restHighLevelClient.update(updateRequest);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
