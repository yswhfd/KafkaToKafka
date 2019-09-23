package com.asiainfo.constant.kafka;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @Classname KafkaEntity
 * @Description TODO
 * @Date 2019/9/12 16:03
 * @Created by Jhon_yh
 */

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
//@Profile({"dev"})
public class KafkaEntity {

//    @Value("${transform.kafka.transform.broker}")
//    private String broker;
    @Value("${transform.kafka.groupId}")
    private String groupId;
//    @Value("${transform.kafka.transform.zookeeper}")
//    private String zookeeper;
//    @Value("${transform.kafka.transform.topic}")
//    private String topicName;
    private Map<String, Object> kafkaConf;
    private Map<String, Object> dispatch_kafkaConf;

    private Map<String, Object> log_kafkaConf;
    @Value("${transform.kafka.log.broker}")
    private String log_kafka_broker;
    @Value("${transform.kafka.log.zookeeper}")
    private String log_kafka_zookeeper;

    @Value("${transform.kafka.send.topic}")
    private String transform_topic;
    private Map<String, Object> transform_kafkaConf;
    @Value("${transform.kafka.send.broker}")
    private String transform_kafka_broker;
    @Value("${transform.kafka.send.zookeeper}")
    private String transform_kafka_zookeeper;
    @Value("${transform.kafka.send.split}")
    private String transform_split;
    @Value("${transform.kafka.scheduler.topic}")
    private String scheduler_topic;
    @Value("${transform.kafka.scheduler.broker}")
    private String scheduler_broker;



    private String ZOOKEEPER_CONNECTION_TIMEOUT_MS = "1000";
    private String SASL_KERBEROS_SERVICE_NAME = "kafka";
    private String SECURITY_PROTOCOL = "SASL_PLAINTEXT";
    private String SASL_MECHANISM = "GSSAPI";
    private String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private String VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    private String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";




}
