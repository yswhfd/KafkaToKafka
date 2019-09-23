package com.asiainfo.utils;

import com.asiainfo.constant.BoncMessBean;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
/**
 * @Classname MysqlUtil
 * @Description no use
 * @Date 2019/9/16 14:31
 * @Created by Jhon_yh
 */

@Slf4j
public class MysqlUtil {

    public static String getSetIdFromSchedulerMysql(String tableName) {
        return "123";
    }

    public static void save2Mysql(Set<BoncMessBean> boncMessSet) {
        CustomHashCode customHashCode = new CustomHashCode();
        String sql = "INSERT INTO `data_audit`.`dv_batch_bonc_to_sche_log` ( `rowkey`, `db_name`, `tb_name`," +
                " `prov_id`, `date_id`, `status`, `message`, `final_status`, `fail_log`, `etl_time` )" +
                " VALUES " +
                " ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        log.debug("saveToMysql sql -> " + sql);
        DateUtil dateUtil = new DateUtil();
        Date date = new Date();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            connection.prepareStatement(sql);
            final int[] count = {0};

//            for (BoncMessBean boncMessage : boncMessSet) {
//                String etlTime = dateUtil.getFormatedTime(date);
//            }
            boncMessSet.forEach((BoncMessBean boncMessage) ->{
                String etlTime = dateUtil.getFormatedTime(date);
                try {
                    preparedStatement.setString(1, customHashCode.getHashCode(boncMessage.getMessage(), 16));
                    preparedStatement.setString(2, boncMessage.getDbName());
                    preparedStatement.setString(3, boncMessage.getTbName());
                    preparedStatement.setString(4, boncMessage.getProvId());
                    preparedStatement.setString(5, boncMessage.getDateId());
                    preparedStatement.setString(6, boncMessage.getStatus());
                    preparedStatement.setString(7, boncMessage.getMessage());
                    preparedStatement.setString(8, boncMessage.getFailLog());
                    preparedStatement.setString(9, boncMessage.getFailLog());
                    preparedStatement.setString(10, etlTime);
                    preparedStatement.addBatch();

                    count[0]++;

                    if (count[0] < 100) {
                        preparedStatement.executeBatch();
                        connection.commit();
                        count[0] = 0;
                    }

                } catch (SQLException e) {
                    log.error("Exception when save log to mysql, error:"+e.getMessage());
                }
            });
            preparedStatement.executeBatch();
            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
