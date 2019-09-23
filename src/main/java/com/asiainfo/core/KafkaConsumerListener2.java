package com.asiainfo.core;


import com.asiainfo.constant.BoncMessBean;
import com.asiainfo.constant.kafka.KafkaEntity;
import com.asiainfo.dao.scheduler.DvBatchBonc2Scheduler;
import com.asiainfo.dao.scheduler.SchedulerDao;
import com.asiainfo.dao.scheduler.UserDao;
import com.asiainfo.entity.scheduler.DvBatchBoncToScheLogEntity;
import com.asiainfo.entity.scheduler.ProvTableEntity;
import com.asiainfo.entity.scheduler.UsersEntity;
import com.asiainfo.utils.DateUtil;
import com.asiainfo.utils.KafkaUtil;
import com.asiainfo.utils.MessageUtil;
import com.asiainfo.utils.MysqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;

/**
 * @Classname KafkaConsumerListener
 * @Description TODO
 * @Date 2019/9/12 16:03
 * @Created by Jhon_yh
 */
@Slf4j
@Component
public class KafkaConsumerListener2 implements InitializingBean {

    @Resource
    KafkaEntity kafkaEntity ;
    @Resource
    KafkaUtil kafkaUtil ;
    @Resource
    MessageUtil messageUtil ;
    @Resource
    BoncMessBean boncMessBean;

    @Resource
    ProvTableEntity provTableEntity;

    @Autowired
    UsersEntity usersEntity;

    @Autowired
    UserDao userDao;

    @Resource
    SchedulerDao schedulerDao;

    @Autowired
    DvBatchBonc2Scheduler dvBatchBonc2Scheduler;

    @Autowired
    DvBatchBoncToScheLogEntity dvBatchBoncToScheLogEntity;



    @Override
    public void afterPropertiesSet() throws Exception {
//        System.out.println("kafkaEntity.broker: " + kafkaEntity.getBroker().toString());
        KafkaConsumer<String, String> consumer = kafkaUtil.getTransformConsumer();
        consumer.subscribe(Arrays.asList(kafkaEntity.getTransform_topic()));
        KafkaProducer schedulerKafkaProducer = kafkaUtil.getSchedulerKafkaProducer(kafkaEntity);


//        for (int i = 0; i < 10; i++) {
//            dvBatchBoncToScheLogEntity.setRowkey(String.format("123456%s",i));
//
//            dvBatchBonc2Scheduler.save(dvBatchBoncToScheLogEntity);
//
//            final DvBatchBoncToScheLogEntity dvBatchBoncToScheLogEntity = dvBatchBonc2Scheduler.findById(this.dvBatchBoncToScheLogEntity.getRowkey()).get();
//            System.out.println("**********************  "+ dvBatchBoncToScheLogEntity.toString());
//        }

//        usersEntity.setId(2);
//        usersEntity.setUsername("test123");
//        usersEntity.setPassword("test123");
//        usersEntity.setGroupId(10);
//        userDao.save(usersEntity);
//
//        final Optional<UsersEntity> byId = userDao.findById(usersEntity.getId());




        while (true){
//            String message = "{\"fileKey\":\"A84923ED294291194E0B1655CD2DC506\",\"provId\":\"842\",\"opTime\":\"2019/01/07 09:29:42\",\"fileName\":\"oldman_comm_habit.20190107.201901.02.000.000.842.CHECK\",\"PrePath\":\"/domain/ns/coll_tmp/coll/842/oldman_comm_habit/201901/\",\"LocalPath\":\"/domain/ns/hubei842/coll/hubei842/oldman_comm_habit/201901/\",\"retryCount\":9,\"mvstatus\":true,\"message\":\"此次移动文件的个数为:1\",\"dbName\":\"hubei842\",\"tableName\":\"oldman_comm_habit\",\"billingCycle\":\"201901\",\"trigerSource\":\"FileMoveTopology\",\"batchNum\":\"842_oldman_comm_habit_201901_02\",\"downGroupId\":\"zq_down_842\",\"daConfKey\":\"dab2270733977d6f7484a294cce26828\"}";
//            boncMessBean = messageUtil.handleMessage(message, kafkaUtil, schedulerKafkaProducer, kafkaEntity);
//            BeanUtils.copyProperties(boncMessBean, dvBatchBoncToScheLogEntity);
//            dvBatchBoncToScheLogEntity.setRowkey(String.format(boncMessBean.getRowKey()+ "_" + "%s",new DateUtil().getTimeStamp()));
//            dvBatchBoncToScheLogEntity.setEtlTime(Timestamp.valueOf(new DateUtil().getFormatedTime(new Date())));
//            log.info(String.format("boncMessBean: %s" , boncMessBean.toString()));
//
            List dvBatchBoncToScheLogEntities = new ArrayList<DvBatchBoncToScheLogEntity>();
//            dvBatchBoncToScheLogEntities.add(dvBatchBoncToScheLogEntity);
//            //保存到mysql
////            dvBatchBonc2Scheduler.save(dvBatchBoncToScheLogEntity);
//            dvBatchBonc2Scheduler.saveAll(dvBatchBoncToScheLogEntities);
//
//            log.info(String.format("dvBatchBoncToScheLogEntity: %s" ,dvBatchBoncToScheLogEntity.toString()));

            Thread.sleep(10000);
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
            records.forEach(record -> {

                boncMessBean = messageUtil.handleMessage(record.value(), kafkaUtil, schedulerKafkaProducer, kafkaEntity);
                BeanUtils.copyProperties(boncMessBean, dvBatchBoncToScheLogEntity);
                dvBatchBoncToScheLogEntity.setRowkey(String.format(boncMessBean.getRowKey()+ "_" + "%s",new DateUtil().getTimeStamp()));
                dvBatchBoncToScheLogEntity.setEtlTime(Timestamp.valueOf(new DateUtil().getFormatedTime(new Date())));
                log.info(String.format("boncMessBean: %s" , boncMessBean.toString()));

                dvBatchBoncToScheLogEntities.add(dvBatchBoncToScheLogEntity);
                //保存到mysql
        //            dvBatchBonc2Scheduler.save(dvBatchBoncToScheLogEntity);
                dvBatchBonc2Scheduler.saveAll(dvBatchBoncToScheLogEntities);

                log.info(String.format("dvBatchBoncToScheLogEntity: %s" ,dvBatchBoncToScheLogEntity.toString()));

            });

            System.out.println(consumer.toString());
        }
    }
}
