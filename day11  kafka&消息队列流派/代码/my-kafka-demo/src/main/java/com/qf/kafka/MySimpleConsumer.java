package com.qf.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class MySimpleConsumer {


  private final static String TOPIC_NAME = "my-replicated-topic";
  private final static String CONSUMER_GROUP_NAME = "testGroup";

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.253.38:9092,172.16.253.38:9093,172.16.253.38:9094");
    // 消费分组名
    props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // 是否自动提交offset，默认就是true
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 自动提交offset的间隔时间
//    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    //创建一个消费者的客户端
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    // 消费者订阅主题列表
    consumer.subscribe(Arrays.asList(TOPIC_NAME));

    while (true) {
      /*
       * poll() API 是拉取消息的长轮询
       */
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("收到消息：partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),
          record.offset(), record.key(), record.value());
      }
      //所有的消息已消费完
      if (records.count() > 0) {//有消息
        // 手动同步提交offset，当前线程会阻塞直到offset提交成功
        // 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
        consumer.commitSync();//=======阻塞=== 提交成功

        // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
        consumer.commitAsync(new OffsetCommitCallback() {
          @Override
          public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
            if (exception != null) {
              System.err.println("Commit failed for " + offsets);
              System.err.println("Commit failed exception: " + exception.getStackTrace());
            }
          }
        });

      }
    }
  }

}
