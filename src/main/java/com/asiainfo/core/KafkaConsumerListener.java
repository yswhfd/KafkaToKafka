//package com.asiainfo.core;
//
//
//import com.asiainfo.constant.BoncMessBean;
//import com.asiainfo.constant.kafka.KafkaEntity;
//import com.asiainfo.dao.scheduler.SchedulerDao;
//import com.asiainfo.entity.scheduler.ProvTableEntity;
//import com.asiainfo.utils.KafkaUtil;
//import com.asiainfo.utils.MessageUtil;
//import com.asiainfo.utils.MysqlUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @Classname KafkaConsumerListener
// * @Description TODO
// * @Date 2019/9/12 16:03
// * @Created by Jhon_yh
// */
//@Slf4j
//@Component
//public class KafkaConsumerListener {
//
//    @Resource
//    KafkaEntity kafkaEntity ;
//    @Resource
//    KafkaUtil kafkaUtil ;
//    @Resource
//    MessageUtil messageUtil ;
//    @Resource
//    BoncMessBean boncMessBean;
//
//    @Resource
//    ProvTableEntity provTableEntity;
//
//    @Resource
//    SchedulerDao schedulerDao;
//
//    @Bean
//    public CommandLineRunner sayHello() throws InterruptedException {
//
//        return (String... args) -> {
//            System.out.println("kafkaEntity.broker: " + kafkaEntity.getBroker().toString());
//            KafkaConsumer<String, String> consumer = kafkaUtil.getTransformConsumer();
//            consumer.subscribe(Arrays.asList(kafkaEntity.getTransform_topic()));
//            KafkaProducer schedulerKafkaProducer = kafkaUtil.getSchedulerKafkaProducer(kafkaEntity);
//
//            provTableEntity.setProvId("835");
//            ProvTableEntity byProvId = schedulerDao.findById(provTableEntity.getProvId()).get();
//            provTableEntity.setProvId("test123");
//            provTableEntity.setProvName("test123");
//            provTableEntity.setSrcFlag("T");
//            provTableEntity.setTarDbName("test123");
//            final ProvTableEntity save = schedulerDao.save(provTableEntity);
////            final ProvTableEntity byId = schedulerDao.findByProvId(835);
//
//            System.out.println("**************** "+byProvId.toString());
//            System.out.println("**************** save: "+save.toString());
//
//
//            while (true){
//                String message = "{\"fileKey\":\"A84923ED294291194E0B1655CD2DC506\",\"provId\":\"842\",\"opTime\":\"2019/01/07 09:29:42\",\"fileName\":\"oldman_comm_habit.20190107.201901.02.000.000.842.CHECK\",\"PrePath\":\"/domain/ns/coll_tmp/coll/842/oldman_comm_habit/201901/\",\"LocalPath\":\"/domain/ns/hubei842/coll/hubei842/oldman_comm_habit/201901/\",\"retryCount\":9,\"mvstatus\":true,\"message\":\"此次移动文件的个数为:1\",\"dbName\":\"hubei842\",\"tableName\":\"oldman_comm_habit\",\"billingCycle\":\"201901\",\"trigerSource\":\"FileMoveTopology\",\"batchNum\":\"842_oldman_comm_habit_201901_02\",\"downGroupId\":\"zq_down_842\",\"daConfKey\":\"dab2270733977d6f7484a294cce26828\"}";
////                boncMessBean = messageUtil.handleMessage(message, kafkaUtil, schedulerKafkaProducer, kafkaEntity);
//                log.info(String.format(boncMessBean.toString()));
//                Set<BoncMessBean> boncMessSet = new HashSet<>();
//                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100000));
//
//                records.forEach(record -> {
//                    boncMessBean = messageUtil.handleMessage(record.value(), kafkaUtil, schedulerKafkaProducer, kafkaEntity);
//                    boncMessSet.add(boncMessBean);
//                    //保存到mysql
//                    MysqlUtil.save2Mysql(boncMessSet);
//                });
//
//                System.out.println(consumer.toString());
//            }
//
//        };
//    }
//}
