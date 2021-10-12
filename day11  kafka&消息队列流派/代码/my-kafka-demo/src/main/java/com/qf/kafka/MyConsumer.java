package com.qf.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class MyConsumer {

  private final static String TOPIC_NAME = "my-replicated-topic";
  private final static String CONSUMER_GROUP_NAME = "testGroup";

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.253.38:9092,172.16.253.38:9093,172.16.253.38:9094");
    // 消费分组名
    props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_NAME);
    // 是否自动提交offset，默认就是true
    //props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    // 自动提交offset的间隔时间
    //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        /*
        当消费主题的是一个新的消费组，或者指定offset的消费方式，offset不存在，那么应该如何消费
        latest(默认) ：只消费自己启动之后发送到主题的消息
        earliest：第一次从头开始消费，以后按照消费offset记录继续消费，这个需要区别于consumer.seekToBeginning(每次都从头开始消费)
        */
    //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    //consumer给broker发送心跳的间隔时间
    props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
    //kafka如果超过10秒没有收到消费者的心跳，则会把消费者踢出消费组，进行rebalance，把分区分配给其他消费者。
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
    //一次poll最大拉取消息的条数，可以根据消费速度的快慢来设置
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
    //如果两次poll的时间如果超出了30s的时间间隔，kafka会认为其消费能力过弱，将其踢出消费组。将分区分配给其他消费者。-rebalance
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30 * 1000);


    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

    consumer.subscribe(Arrays.asList(TOPIC_NAME));
    // 消费指定分区
//    consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));

    //消息回溯消费
       /* consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        consumer.seekToBeginning(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));*/

    //指定offset消费
        /*consumer.assign(Arrays.asList(new TopicPartition(TOPIC_NAME, 0)));
        consumer.seek(new TopicPartition(TOPIC_NAME, 0), 10);*/

    //从指定时间点开始消费
       /* List<PartitionInfo> topicPartitions = consumer.partitionsFor(TOPIC_NAME);
        //从1小时前开始消费
        long fetchDataTime = new Date().getTime() - 1000 * 60 * 60;
        Map<TopicPartition, Long> map = new HashMap<>();
        for (PartitionInfo par : topicPartitions) {
            map.put(new TopicPartition(TOPIC_NAME, par.partition()), fetchDataTime);
        }
        Map<TopicPartition, OffsetAndTimestamp> parMap = consumer.offsetsForTimes(map);
        for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : parMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndTimestamp value = entry.getValue();
            if (key == null || value == null) continue;
            Long offset = value.offset();
            System.out.println("partition-" + key.partition() + "|offset-" + offset);
            System.out.println();
            //根据消费里的timestamp确定offset
            if (value != null) {
                consumer.assign(Arrays.asList(key));
                consumer.seek(key, offset);
            }
        }*/

    while (true) {
      /*
       * poll() API 是拉取消息的长轮询
       */
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("收到消息：partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),
          record.offset(), record.key(), record.value());
      }

            if (records.count() > 0) {
                // 手动同步提交offset，当前线程会阻塞直到offset提交成功
                // 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
                consumer.commitSync();//=======阻塞=== 提交成功

                // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
//                consumer.commitAsync(new OffsetCommitCallback() {
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//                        if (exception != null) {
//                            System.err.println("Commit failed for " + offsets);
//                            System.err.println("Commit failed exception: " + exception.getStackTrace());
//                        }
//                    }
//                });

            }
    }
  }

}
