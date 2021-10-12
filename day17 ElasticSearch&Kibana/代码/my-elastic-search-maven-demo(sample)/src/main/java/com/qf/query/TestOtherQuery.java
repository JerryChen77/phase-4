package com.qf.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.entity.SmsLogs;
import com.qf.util.ESUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestOtherQuery {

  RestHighLevelClient client = ESUtil.getClient();
  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testMatchAll() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
//    builder.from(0);
//    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.prefixQuery("corpName","途虎"));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
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
  public void testFuzzyQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
//    builder.from(0);
//    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.fuzzyQuery("corpName","盒马先生").prefixLength(2));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
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
  public void testWildCardQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
//    builder.from(0);
//    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.wildcardQuery("corpName","中国*"));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
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
  public void testRangeQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
//    builder.from(0);
//    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.rangeQuery("fee").gt(5).lte(10));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    System.out.println(smsLogsList);


  }

  @Test
  public void testRegexpQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    //设置分页
//    builder.from(0);
//    builder.size(5);
    //设置查询的内容
    builder.query(QueryBuilders.regexpQuery("mobile","180[0-9]{8}"));
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.解析查询结果
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    System.out.println(smsLogsList);


  }


}
