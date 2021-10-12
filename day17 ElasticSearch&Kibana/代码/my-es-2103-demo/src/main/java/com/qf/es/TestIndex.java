package com.qf.es;

import com.qf.es.util.ESUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;

import java.io.IOException;

public class TestIndex {

  /**
   * 创建一个索引
   */
  @Test
  public void testCreateIndex() throws IOException {
    //1.访问es的restful接口的客户端
    RestHighLevelClient client = ESUtil.getClient();

    //2.定义索引的名称和类型
    String index = "book_pro";
    String type = "it";


    //3.指明settings
    Settings.Builder settings = Settings.builder()
      .put("number_of_shards", 5)
      .put("number_of_replicas", 1);
    //4.指明mappings
    XContentBuilder mappings = JsonXContent.contentBuilder()
      .startObject()
        .startObject("properties")
          .startObject("name")
            .field("type","text")
            .field("analyzer","ik_max_word")
            .field("index",true)
          .endObject()
          .startObject("author")
            .field("type","keyword")
          .endObject()
        .endObject()
      .endObject();

    //5.创建一个request请求,该请求表示要在es中创建一个索引，需要指明：索引、类型、字段
    CreateIndexRequest request = new CreateIndexRequest(index).settings(settings).mapping(type,mappings);

    //6.用es rest high level client客户端工具来访问es的rest接口--发送请求，得到响应结果
    CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
    System.out.println(response);


  }

}
