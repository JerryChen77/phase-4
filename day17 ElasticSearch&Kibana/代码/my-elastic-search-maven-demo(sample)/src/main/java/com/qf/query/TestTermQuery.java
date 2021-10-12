package com.qf.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.entity.SmsLogs;
import com.qf.util.ESUtil;
import com.qf.util.ObjectXMapperUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestTermQuery {

  RestHighLevelClient client = ESUtil.getClient();
  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testTerm() throws Exception {




    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
    builder.from(0);
    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.termQuery("province","北京"));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
    /*

     */
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      //map->object
//      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//      SmsLogs smsLogs = (SmsLogs) ObjectXMapperUtil.mapToObject(sourceAsMap, SmsLogs.class);
//      smsLogsList.add(smsLogs);
      //json->object
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    System.out.println(smsLogsList);


  }

  @Test
  public void testTerms() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
    builder.from(0);
    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.termsQuery("province","北京","山西"));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
    /*

     */
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      //map->object
//      Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//      SmsLogs smsLogs = (SmsLogs) ObjectXMapperUtil.mapToObject(sourceAsMap, SmsLogs.class);
//      smsLogsList.add(smsLogs);
      //json->object
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    System.out.println(smsLogsList);


  }



}
