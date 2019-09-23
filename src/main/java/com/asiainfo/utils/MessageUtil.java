package com.asiainfo.utils;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.constant.BoncMessBean;
import com.asiainfo.constant.kafka.KafkaEntity;
import com.asiainfo.dao.zq.TableDefinition;
import com.asiainfo.entity.zq.TableDefinitionEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Classname MessageUtil
 * @Description TODO
 * @Date 2019/9/16 11:28
 * @Created by Jhon_yh
 */

@Component
@Slf4j
public class MessageUtil {
    @Resource
    JsonUtil jsonUtil ;
    //    JsonUtil jsonUtil = new JsonUtil();
    @Resource
    BoncMessBean boncMessBean;

    @Resource
    TableDefinition tableDefinition;

    @Resource
    TableDefinitionEntity tableDefinitionEntity;

    /**
     * 解析国信消息并转成bean
     * {"fileKey":"A84923ED294291194E0B1655CD2DC506","provId":"842","opTime":"2019/01/07 09:29:42","fileName":"oldman_comm_habit.20190107.201901.02.000.000.842.CHECK","PrePath":"/domain/ns/coll_tmp/coll/842/oldman_comm_habit/201901/","LocalPath":"/domain/ns/hubei842/coll/hubei842/oldman_comm_habit/201901/","retryCount":9,"mvstatus":true,"message":"此次移动文件的个数为:1","dbName":"hubei842","tableName":"oldman_comm_habit","billingCycle":"201901","trigerSource":"FileMoveTopology","batchNum":"842_oldman_comm_habit_201901_02","downGroupId":"zq_down_842","daConfKey":"dab2270733977d6f7484a294cce26828"}
     * 国信消息体内容：
     * 0   fileKey      主 键
     * 1   provId 			省份编码
     * 2   opTime       操作时间
     * 3   fileName     文件名(checks文件名)
     * 4   PrePath      HDFS下载路径(pre)(含账期)
     * 5   LocalPath    HDFS下载路径(itf)(含账期)
     * 6   retryCount   重试次数
     * 7   mvstatus     mv状态
     * 8   message      处理结果
     * 9   dbName       库名
     * 10   tableName    表名
     * 11   billingCycle 账期
     * 12   trigerSource 触发源(storm/前台)
     * 13   batchNum
     * 14   downGroupId  下载ID
     * 15   daConfKey    配置表主键(关联使用)
     *
     //     * @param message
     */
//    public BoncMessBean resolveBoncMes(String message, BoncMessBean boncMessBean) {
//        log.info("start to resolve boncMessage");
//        final JSONObject mesJsonObject = jsonUtil.str2JsonObj(message);
//        boncMessBean.setDbName(mesJsonObject.getString("dbName"));
//        boncMessBean.setTbName(mesJsonObject.getString("tableName"));
//        boncMessBean.setProvId(mesJsonObject.getString("provId"));
//        boncMessBean.setDateId(mesJsonObject.getString("billingCycle"));
//        boncMessBean.setStatus(mesJsonObject.getString("mvstatus"));
//        return boncMessBean;
//    }
    public void resolveBoncMes(String message) {
        log.info("start to resolve boncMessage");
        final JSONObject mesJsonObject = jsonUtil.str2JsonObj(message);
        boncMessBean.setDbName(mesJsonObject.getString("dbName"));
        boncMessBean.setTbName(mesJsonObject.getString("tableName"));
        boncMessBean.setProvId(mesJsonObject.getString("provId"));
        boncMessBean.setDateId(mesJsonObject.getString("billingCycle"));
        boncMessBean.setStatus(mesJsonObject.getString("mvstatus"));
    }

    /**
     * 处理失败时写入消息
     *
     * @param failLog
     */
//    public BoncMessBean handleFail(String failLog, BoncMessBean boncMessBean) {
//        boncMessBean.setFinalStatus("F");
//        boncMessBean.setFailLog(failLog);
//        log.error(String.format("finalStatus is : %s failLog is : %s", "F", failLog));
//        return boncMessBean;
//    }
    public void handleFail(String failLog) {
        boncMessBean.setFinalStatus("F");
        boncMessBean.setFailLog(failLog);
        log.error(String.format("finalStatus is : %s failLog is : %s", "F", failLog));
    }

    /**
     * 处理消息
     *
     * @param message
     * @param kafkaUtil
     * @param producer
     * @return
     */
    public BoncMessBean handleMessage(String message, KafkaUtil kafkaUtil, KafkaProducer<String, String> producer, KafkaEntity kafkaEntity) {
        //开始处理,转换格式
        try {
            final CustomHashCode customHashCode = new CustomHashCode();
            boncMessBean.setMessage(message);
            resolveBoncMes(message);
            log.info("message is : " + message);
            //生成调度消息
            String schedulerMessage = generateSchedulerMessage(boncMessBean);

            //给调度发消息
            kafkaUtil.send(producer, kafkaEntity.getScheduler_topic(), schedulerMessage);
            log.info(String.format("message send successfully ...message is: %s", message));
            boncMessBean.setRowKey(customHashCode.getHashCode(boncMessBean.getMessage(), 16));
            boncMessBean.setFinalStatus("S");
            boncMessBean.setMessage(schedulerMessage);
        } catch (Exception e) {
            log.error("handleMessage: " + e.getMessage() );
            handleFail(e.getMessage());
        }
        return boncMessBean;
    }


    /**
     * 转换成给调度发送的格式
     * 样例：{"dataSet":[{"subscribe":[{"timeslice":"20180708","area_id":"843"}],"setBlock":["prov_id:843","year:2018","month:12","day:01","hour:00"],"setID":"e336ecceac809c459f08f8e886e6b14d","storage":"table"}],"version":"1.1"}
     * {
     * "dataSet": [{
     * "subscribe": [{
     * "timeslice": "20180708",
     * "area_id": "843"
     * }],
     * "setBlock": ["prov_id:843", "year:2018", "month:12", "day:01", "hour:00"],
     * "setID": "e336ecceac809c459f08f8e886e6b14d",
     * "storage": "table"
     * }],
     * "version": "1.1"
     * }
     * //01-09 大冯提出发送的消息里setBlock里格式为year month day hour分开写.
     */
    private String generateSchedulerMessage(BoncMessBean boncMessBean) {
        log.info("start to generate scheduler message ... ...");
        String tableName = String.format("%s.%s", boncMessBean.getDbName(), boncMessBean.getTbName());
        String timeSlice = boncMessBean.getDateId();
        String areaId = boncMessBean.getProvId();
        String provId = boncMessBean.getProvId();

        String year = "", month = "", day = "00", hour = "00", min = "00";
        try {
            switch (timeSlice.length()){
                case 6:
                    year = timeSlice.substring(0, 4);
                    month = timeSlice.substring(4, 6);
                    break;
                case 8:
                    year = timeSlice.substring(0, 4);
                    month = timeSlice.substring(4, 6);
                    day = timeSlice.substring(6, 8);
                    break;
                case 10:
                    year = timeSlice.substring(0, 4);
                    month = timeSlice.substring(4, 6);
                    day = timeSlice.substring(6, 8);
                    hour = timeSlice.substring(8, 10);
                    break;
                case 12:
                    year = timeSlice.substring(0, 4);
                    month = timeSlice.substring(4, 6);
                    day = timeSlice.substring(6, 8);
                    hour = timeSlice.substring(8, 10);
                    min = timeSlice.substring(10, 12);
                    break;
                default:
                        throw new Exception("账期参数格式不正确！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

//        scheduler.table_definition 在这个表里面查表的setID
//        String setId = MysqlUtil.getSetIdFromSchedulerMysql(tableName).trim();

        TableDefinitionEntity byTablename = tableDefinition.findByTablename(tableName);
        String setId = byTablename.getTableid().trim();

        log.info(String.format("setId is : %s ", setId));

        if ("".equals(setId)) {
            throw new RuntimeException(String.format("%s setId is null", tableName));
        }

        if (!"true".equals(boncMessBean.getStatus())) {
            throw new RuntimeException("采集状态为失败，不作处理");
        }

        String schedulerMessage = String.format("{\"dataSet\":[{\"subscribe\":[{\"timeslice\":\"%s\",\"area_id\":\"%s\"}],\"setBlock\":[\"prov_id:%s\",\"year:%s\",\"month:%s\",\"day:%s\",\"hour:%s\",\"min:%s\"],\"setID\":\"%s\",\"storage\":\"table\"}],\"version\":\"1.1\"}",
                timeSlice, areaId, provId, year, month, day, hour, min, setId);

        log.info(String.format("message is : %s", schedulerMessage));
        return schedulerMessage;
    }


    public static void main(String[] args) {
        MessageUtil messageUtil = new MessageUtil();


    }


}
