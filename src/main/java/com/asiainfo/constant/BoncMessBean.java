package com.asiainfo.constant;

/**
 * 国信消息体.
 * 分别对应：库名、表名、省份编码、账期、状态、国信发送的完整消息、最终处理结果(S/F)、失败原因.
 *

 *
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Classname BoncMessBean
 * @Description TODO
 * @Date 2019/9/12 16:03
 * @Created by Jhon_yh
 */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BoncMessBean {

    private String rowKey;
    private String dbName;
    private String tbName;
    private String provId;
    private String dateId;
    private String status;
    private String message;
    private String finalStatus;
    private String failLog;


    public String toString(){
        return  String.format(
                "rowKey = %s " +
                "dbName = %s " +
                "tbName = %s " +
                "provId = %s " +
                "dateId = %s " +
                "finalStatus = %s " +
                "failLog = %s", this.rowKey,this.dbName, this.tbName, this.provId, this.dateId, this.finalStatus, this.failLog);

    }

    public static void main(String[] args) {
//        String mess = "{\"fileKey\":\"A84923ED294291194E0B1655CD2DC506\",\"provId\":\"842\",\"opTime\":\"2019/01/07 09:29:42\",\"fileName\":\"oldman_comm_habit.20190107.201901.02.000.000.842.CHECK\",\"PrePath\":\"/domain/ns/coll_tmp/coll/842/oldman_comm_habit/201901/\",\"LocalPath\":\"/domain/ns/hubei842/coll/hubei842/oldman_comm_habit/201901/\",\"retryCount\":9,\"mvstatus\":true,\"message\":\"此次移动文件的个数为:1\",\"dbName\":\"hubei842\",\"tableName\":\"oldman_comm_habit\",\"billingCycle\":\"201901\",\"trigerSource\":\"FileMoveTopology\",\"batchNum\":\"842_oldman_comm_habit_201901_02\",\"downGroupId\":\"zq_down_842\",\"daConfKey\":\"dab2270733977d6f7484a294cce26828\"}";
//        BoncMessBean boncMessBean = new BoncMessBean();
////        boncMessBean.resolveBoncMes(mess);
//        System.out.println(boncMessBean.toString());



    }

}
