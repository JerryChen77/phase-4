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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestBoolQuery {

  RestHighLevelClient client = ESUtil.getClient();
  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testBoolQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.设置scroll缓冲区的存活时间
    request.scroll(TimeValue.timeValueMinutes(1));

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();

    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    /*
    # 查询省份为武汉或者北京
    # 运营商不是联通
    # smsContent中包含中国和平安
     */
    boolQueryBuilder.should(QueryBuilders.termQuery("province","北京"));
    boolQueryBuilder.should(QueryBuilders.termQuery("province","武汉"));
    boolQueryBuilder.mustNot(QueryBuilders.termQuery("operatorId",2));
    boolQueryBuilder.must(QueryBuilders.matchQuery("smsContent","中国"));
    boolQueryBuilder.must(QueryBuilders.matchQuery("smsContent","平安"));


    //设置查询的内容
    builder.query(boolQueryBuilder);
    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);


    List<SmsLogs> smsLogsList = getSmsLogs(response);
    System.out.println(smsLogsList.size()+":"+smsLogsList);



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
