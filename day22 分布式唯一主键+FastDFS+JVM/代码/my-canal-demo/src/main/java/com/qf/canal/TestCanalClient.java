package com.qf.canal;

import java.net.InetSocketAddress;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.qf.canal.util.RedisUtil;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
public class TestCanalClient {

  public static void main(String args[]) {

    // 创建链接，hostname位canal服务器ip port位canal服务器端口，username，password可不填
    CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1",
      11111), "example", "", "");
    int batchSize = 1000;
    try {
      connector.connect();
      connector.subscribe(".*\\..*");
      connector.rollback();
      while (true) {
        Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
        long batchId = message.getId();
        int size = message.getEntries().size();
        if (batchId == -1 || size == 0) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          printEntry(message.getEntries());
        }

        connector.ack(batchId); // 提交确认
        // connector.rollback(batchId); // 处理失败, 回滚数据
      }

    } finally {
      connector.disconnect();
    }
  }

  private static void printEntry(List<Entry> entrys) {
    for (Entry entry : entrys) {
      if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
        continue;
      }

      RowChange rowChage = null;
      try {
        rowChage = RowChange.parseFrom(entry.getStoreValue());
      } catch (Exception e) {
        throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
          e);
      }

      EventType eventType = rowChage.getEventType();
      System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
        entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
        entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
        eventType));

      for (RowData rowData : rowChage.getRowDatasList()) {
        if (eventType == EventType.DELETE) {
          redisDelete(rowData.getBeforeColumnsList());
        } else if (eventType == EventType.INSERT) {
          redisInsert(rowData.getAfterColumnsList());
        } else {
          System.out.println("-------> before");
          printColumn(rowData.getBeforeColumnsList());
          System.out.println("-------> after");
          //对于热点数据防止高并发下的redis的脏读的出现。那么可以用先更新数据库，后删除缓存的策略。
          redisDelete(rowData.getBeforeColumnsList());
//          redisUpdate(rowData.getAfterColumnsList());
          printColumn(rowData.getAfterColumnsList());
        }
      }
    }
  }

  /**
   * 打印变化的数据
   *
   * @param columns
   */
  private static void printColumn(List<Column> columns) {
    for (Column column : columns) {
      System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
    }
  }

  /**
   * 数据插入同步redis
   *
   * @param columns
   */
  private static void redisInsert(List<Column> columns) {
    JSONObject json = new JSONObject();
    for (Column column : columns) {
      json.put(column.getName(), column.getValue());
    }
    if (columns.size() > 0) {
      RedisUtil.stringSet("user:" + columns.get(0).getValue(), json.toJSONString());
    }
  }

  /**
   * 更新同步redis
   *
   * @param columns
   */
  private static void redisUpdate(List<Column> columns) {
    JSONObject json = new JSONObject();
    for (Column column : columns) {
      json.put(column.getName(), column.getValue());
    }
    if (columns.size() > 0) {
      RedisUtil.stringSet("user:" + columns.get(0).getValue(), json.toJSONString());
    }
  }

  /**
   * 数据删除同步redis
   *
   * @param columns
   */
  private static void redisDelete(List<Column> columns) {
    JSONObject json = new JSONObject();
    for (Column column : columns) {
      json.put(column.getName(), column.getValue());
    }
    if (columns.size() > 0) {
      RedisUtil.delKey("user:" + columns.get(0).getValue());
    }
  }



}
