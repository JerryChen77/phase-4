package com.qf;

import com.alibaba.fastjson.JSON;
import com.qf.entity.Book;
import com.qf.util.ESUtil;
import com.qf.util.ObjectXMapperUtil;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;

import java.io.IOException;

public class ESTest {

  /**
   * 创建索引:settings\mappings
   */
  @Test
  public void createIndex() throws IOException {
    //1.获得操作es的客户端工具
    RestHighLevelClient client = ESUtil.getClient();
    //2.指明settings
    Settings.Builder settings = Settings.builder()
      .put("number_of_shards", 5)
      .put("number_of_replicas", 1);
    //3.指明mappings
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
    //4.索引的名称
    String index = "book_pro";
    String type = "it";
    //5.创建一个请求，这个请求用来创建索引,指明index，settings，type，mappings
    CreateIndexRequest request = new CreateIndexRequest(index)
                                      .settings(settings)
                                      .mapping(type,mappings);

    //6.用es客户端去发这个请求,获得响应的结果
    CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
    System.out.println(response);
  }


  /**
   * 向es添加数据
   *
   * POST /索引/类型/id
   *
   * {}
   */
  @Test
  public void insertDataToES() throws IOException {

    //0.获得操作es的客户端工具
    RestHighLevelClient client = ESUtil.getClient();

    String index = "book_pro";
    String type = "it";

    Book book = new Book();
    book.setId(1001l);
    book.setName("jinpinmei");
    book.setAuthor("xiaoma");

    //1.把book对象转换成json字符串。
    String json = JSON.toJSONString(book);
    //2.需要插入数据的request对象
    IndexRequest request = new IndexRequest(index,type,String.valueOf(book.getId()));
    //3.封装具体的数据
    request.source(json, XContentType.JSON);
    //4.发送请求
    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());


  }

  /**
   * 修改es中的文档
   */
  @Test
  public void testUpdate() throws IOException {

    //0.获得操作es的客户端工具
    RestHighLevelClient client = ESUtil.getClient();

    String index = "book_pro";
    String type = "it";

    Book book = new Book();
    book.setId(1001l);
    book.setName("java入门");
    book.setAuthor("xiaoma");

    //1.封装一个修改的request对象
    UpdateRequest request = new UpdateRequest(index,type,String.valueOf(book.getId()));
    //2.携带数据:工具类：Object2Map common->util
    request.doc(ObjectXMapperUtil.objectToMap(book));

    //3.执行这个请求
    UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());


  }

  @Test
  public void testDel() throws IOException {

    //0.获得操作es的客户端工具
    RestHighLevelClient client = ESUtil.getClient();

    String index = "book_pro";
    String type = "it";
    Long id = 1001l;

    //1.封装一个删除数据的request
    DeleteRequest request = new DeleteRequest(index,type,String.valueOf(id));
    //2.发送请求
    DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());


  }




}
