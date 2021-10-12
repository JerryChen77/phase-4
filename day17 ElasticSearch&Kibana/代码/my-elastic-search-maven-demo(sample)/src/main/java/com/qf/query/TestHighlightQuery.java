package com.qf.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.entity.SmsLogs;
import com.qf.util.ESUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestHighlightQuery {

  RestHighLevelClient client = ESUtil.getClient();
  String index = "sms-logs-index";
  String type = "sms-logs-type";

  @Test
  public void testHighLightQuery() throws Exception {
    //1.封装一个查询的request对象
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2.设置scroll缓冲区的存活时间
    request.scroll(TimeValue.timeValueMinutes(1));

    //2.封装查询到的条件
    SearchSourceBuilder builder = new SearchSourceBuilder();

    //2.1设置查询的内容
    builder.query(QueryBuilders.matchQuery("smsContent","盒马"));

    //2.2 设置高亮的配置
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    highlightBuilder.field("smsContent",10)
      .preTags("<span style='color:red'>")
      .postTags("</span>");
    builder.highlighter(highlightBuilder);

    //3.将查询条件封装进request请求对象中
    request.source(builder);

    //4.发送请求
    SearchResponse response = client.search(request, RequestOptions.DEFAULT);


    List<SmsLogs> smsLogsList = getHighLightResult(response);
    System.out.println(smsLogsList.size()+":"+smsLogsList);



  }

  private List<SmsLogs> getHighLightResult(SearchResponse response) throws com.fasterxml.jackson.core.JsonProcessingException {
    //5.解析查询结果
    ObjectMapper objectMapper = new ObjectMapper();
    List<SmsLogs> smsLogsList = new ArrayList<>();
    SearchHit[] hits = response.getHits().getHits();
    for (SearchHit hit : hits) {
      String json = hit.getSourceAsString();
      SmsLogs smsLogs = objectMapper.readValue(json, SmsLogs.class);
      //获得高亮部分的内容
      Map<String, HighlightField> map = hit.getHighlightFields();
      HighlightField smsContent = map.get("smsContent");
      Text[] fragments = smsContent.getFragments();
      StringBuffer stringBuffer = new StringBuffer();
      for (Text text : fragments) {
        stringBuffer.append(text.toString());
      }
      //更新到对象中
      smsLogs.setSmsContent(stringBuffer.toString());
      smsLogsList.add(smsLogs);
    }
    return smsLogsList;
  }


  @Test
  public void cardinality() throws IOException {
    //1. 创建SearchRequest
    SearchRequest request = new SearchRequest(index);
    request.types(type);

    //2. 指定使用的聚合查询方式
    SearchSourceBuilder builder = new SearchSourceBuilder();
    builder.aggregation(AggregationBuilders.cardinality("agg1111").field("province"));

    builder.aggregation(AggregationBuilders.range("agg2222").field("fee")
      .addUnboundedTo(5)
      .addRange(5,10)
      .addUnboundedFrom(10));

    request.source(builder);

    //3. 执行查询
    SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

    //4. 获取返回结果
    Cardinality agg = resp.getAggregations().get("agg1111");
    long value = agg.getValue();
    System.out.println(value);
    System.out.println("==============");
    Range agg2222 = resp.getAggregations().get("agg2222");
    for (Range.Bucket bucket : agg2222.getBuckets()) {
      String key = bucket.getKeyAsString();
      Object from = bucket.getFrom();
      Object to = bucket.getTo();
      long docCount = bucket.getDocCount();
      System.out.println(String.format("key：%s，from：%s，to：%s，docCount：%s",key,from,to,docCount));
    }

  }


}
