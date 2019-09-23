package com.asiainfo.utils;


import com.asiainfo.constant.kafka.KafkaEntity;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Classname KafkaUtil
 * @Description   the util of kafka u can use it to getProducer of consumer for topics
 * @Date 2019/9/12 16:03
 * @Created by Jhon_yh
 */
/**

 *
 */
@Component
public class KafkaUtil {

    @Autowired
    KafkaEntity kafkaEntity = new KafkaEntity();


//    public void initKafka(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("metadata.broker.list", kafkaEntity.getBroker());
//        map.put("bootstrap.servers", kafkaEntity.getBroker());
//        map.put("zookeeper.connect", kafkaEntity.getZookeeper());
//        map.put("group.id", kafkaEntity.getGroupId());
//        map.put("zookeeper.connection.timeout.ms", kafkaEntity.getZOOKEEPER_CONNECTION_TIMEOUT_MS());
////        map.put("sasl.kerberos.service.name", kafkaEntity.getSASL_KERBEROS_SERVICE_NAME());
////        map.put("security.protocol", kafkaEntity.getSECURITY_PROTOCOL());
////        map.put("sasl.mechanism", kafkaEntity.getSASL_MECHANISM());
//        map.put("key.deserializer", kafkaEntity.getKEY_DESERIALIZER());
//        map.put("value.deserializer", kafkaEntity.getVALUE_DESERIALIZER());
//        kafkaEntity.setDispatch_kafkaConf(map);
//    }

    /**
     * 本地认证不能通过--
     * @return
     */
    public KafkaConsumer<String, String> getTransformConsumer(){

        Properties properties = new Properties();
        properties.put("metadata.broker.list", kafkaEntity.getTransform_kafka_broker());
        properties.put("bootstrap.servers", kafkaEntity.getTransform_kafka_broker());
        properties.put("zookeeper.connect", kafkaEntity.getTransform_kafka_zookeeper());
        properties.put("group.id", kafkaEntity.getGroupId());
        properties.put("zookeeper.connection.timeout.ms", kafkaEntity.getZOOKEEPER_CONNECTION_TIMEOUT_MS());
        properties.put("sasl.kerberos.service.name", kafkaEntity.getSASL_KERBEROS_SERVICE_NAME());
        properties.put("security.protocol", kafkaEntity.getSECURITY_PROTOCOL());
        properties.put("sasl.mechanism", kafkaEntity.getSASL_MECHANISM());
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", kafkaEntity.getKEY_DESERIALIZER());
        properties.put("value.deserializer", kafkaEntity.getVALUE_DESERIALIZER());

        return new KafkaConsumer(properties);
    }

    /**
     * 获取调度kafka生产者
     * @param kafkaEntity
     * @return KafkaProducer
     */
    public KafkaProducer getSchedulerKafkaProducer(KafkaEntity kafkaEntity){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaEntity.getScheduler_broker());
        properties.put("acks", "all");
        properties.put("retries", "0");
        properties.put("batch.size", "16384");
        properties.put("linger.ms", "1");
        properties.put("buffer.memory", "33554432");
        properties.put("key.serializer", kafkaEntity.getKEY_SERIALIZER());
        properties.put("value.serializer", kafkaEntity.getVALUE_SERIALIZER());
        properties.put("sasl.kerberos.service.name", kafkaEntity.getSASL_KERBEROS_SERVICE_NAME());
        properties.put("security.protocol", kafkaEntity.getSECURITY_PROTOCOL());
        properties.put("sasl.mechanism", kafkaEntity.getSASL_MECHANISM());
        return new KafkaProducer(properties);
    }

    /**
     * send message to Kafka
     * @param kafkaProducer
     * @param topic
     * @param mess
     */
    public void send(KafkaProducer kafkaProducer, String topic, String mess){
        kafkaProducer.send(new ProducerRecord(topic, mess));
    }




}


