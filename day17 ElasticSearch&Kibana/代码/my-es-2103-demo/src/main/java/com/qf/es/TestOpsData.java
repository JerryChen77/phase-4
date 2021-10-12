package com.qf.es;

import com.alibaba.fastjson.JSON;
import com.qf.es.entity.Book;
import com.qf.es.util.ESUtil;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;

public class TestOpsData {

  @Test
  public void testInsertDataToES() throws IOException {

    //1.获得一个客户端
    RestHighLevelClient client = ESUtil.getClient();

    //2.指明索引和类型
    String index = "book_pro"; //name author
    String type = "it";

    //3.准备数据
    Book book = new Book();
    book.setId(1001L);
    book.setName("言情小说");
    book.setAuthor("小高");

    //转换成json
    String json = JSON.toJSONString(book);

    //4.准备一个request对象
    IndexRequest request = new IndexRequest(index,type,book.getId().toString());
    request.source(json, XContentType.JSON);

    //5.用client去发送request——restful 得到响应结果
    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());
  }


  @Test
  public void testUpdateDataToES() throws IOException {

    //1.获得一个客户端
    RestHighLevelClient client = ESUtil.getClient();

    //2.指明索引和类型
    String index = "book_pro"; //name author
    String type = "it";

    //3.准备数据
    Book book = new Book();
    book.setId(1001L);
    book.setName("写真");
    book.setAuthor("小高");

    //转换成json
    String json = JSON.toJSONString(book);

    //4.准备一个更新数据的request对象
    UpdateRequest request = new UpdateRequest(index,type,book.getId().toString());
    request.doc(json,XContentType.JSON);

    //5.用client去发送更新数据的request——restful 得到响应结果
//    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());
  }


  @Test
  public void testDeleteDataToES() throws IOException {

    //1.获得一个客户端
    RestHighLevelClient client = ESUtil.getClient();

    //2.指明索引和类型
    String index = "book_pro"; //name author
    String type = "it";

    //4.准备一个更新数据的request对象
//    UpdateRequest request = new UpdateRequest(index,type,book.getId().toString());
    DeleteRequest request = new DeleteRequest(index,type,"1001");



    //5.用client去发送更新数据的request——restful 得到响应结果
//    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//    UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
    DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
    System.out.println(response.getResult());
  }


}
