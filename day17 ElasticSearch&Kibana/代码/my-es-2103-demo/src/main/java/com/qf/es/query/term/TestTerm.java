package com.qf.es.query.term;

import com.alibaba.fastjson.JSONObject;
import com.qf.es.util.ESUtil;
import com.qf.es.util.SmsLogs;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestTerm {

  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testTerm() throws IOException {

    //1.创建一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.封装查询条件
    SearchSourceBuilder builder = new SearchSourceBuilder();
    builder.from(0);
    builder.size(5);
    QueryBuilder query = QueryBuilders.termQuery("province","北京");
    builder.query(query);

    //3.将查询条件关联进request里
    request.source(builder);

    //4.用client发送请求，拿到响应
    RestHighLevelClient client = ESUtil.getClient();
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);

    //5.response把结果解析出来放到一个集合里去
    SearchHits hits = response.getHits();
    SearchHit[] hitss = hits.getHits();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    for (SearchHit documentFields : hitss) {
      String json = documentFields.getSourceAsString();
      SmsLogs smsLogs = JSONObject.parseObject(json, SmsLogs.class);
      smsLogsList.add(smsLogs);
    }
    System.out.println(smsLogsList);


  }






}
