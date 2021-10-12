package com.qf.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MySimpleProducer {

  private final static String TOPIC_NAME = "my-replicated-topic";

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    //1.设置参数
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.253.38:9092,172.16.253.38:9093,172.16.253.38:9094");

    //把发送的key从字符串序列化为字节数组
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    //把发送消息value从字符串序列化为字节数组
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    //2.创建生产消息的客户端，传入参数
    Producer<String,String> producer = new KafkaProducer<String, String>(props);

    //3.创建消息
    //key：作用是决定了往哪个分区上发，value：具体要发送的消息内容
    ProducerRecord<String,String> producerRecord = new ProducerRecord<>(TOPIC_NAME,"mykeyvalue","hellokafka");

    //4.发送消息,得到消息发送的元数据并输出
    RecordMetadata metadata = producer.send(producerRecord).get();
    System.out.println("同步方式发送消息结果：" + "topic-" + metadata.topic() + "|partition-"
      + metadata.partition() + "|offset-" + metadata.offset());



  }


}
