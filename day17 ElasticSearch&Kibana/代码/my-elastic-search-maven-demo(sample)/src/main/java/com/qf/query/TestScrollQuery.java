package com.qf.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.entity.SmsLogs;
import com.qf.util.ESUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestScrollQuery {

  RestHighLevelClient client = ESUtil.getClient();
  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testMatchAll() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.设置scroll缓冲区的存活时间
    request.scroll(TimeValue.timeValueMinutes(1));

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置查询的内容
    builder.query(QueryBuilders.matchAllQuery());
    //设置每次获得的条数
    builder.size(5);
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    String scrollId = response.getScrollId();
    //========第一次查询===========
    List<SmsLogs> smsLogsList = getSmsLogs(response);
    System.out.println(smsLogsList.size()+":"+smsLogsList);

    while(true){
      //====之后的每一次======
      SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
      //续命
      searchScrollRequest.scroll(TimeValue.timeValueMinutes(1));
      //执行scroll查询
      SearchResponse scrollResponse = client.scroll(searchScrollRequest, RequestOptions.DEFAULT);
      List<SmsLogs> list = getSmsLogs(scrollResponse);
      if(Objects.isNull(list) || list.size()==0){
        break;
      }
      System.out.println("=========================");
      System.out.println(list.size()+":"+list);


    }


  }

  private List<SmsLogs> getSmsLogs(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
    //5.解析查询结果
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    return smsLogsList;
  }


}
