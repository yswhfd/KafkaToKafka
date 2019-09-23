package com.asiainfo.entity.scheduler;

import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Classname DvBatchBoncToScheLogEntity
 * @Description TODO
 * @Date 2019/9/18 18:26
 * @Created by Jhon_yh
 */
@ToString
@Entity
@Component
@Table(name = "dv_batch_bonc_to_sche_log", schema = "test")
public class DvBatchBoncToScheLogEntity {
    private String rowkey;
    private String dbName;
    private String tbName;
    private String provId;
    private String dateId;
    private String status;
    private String message;
    private String finalStatus;
    private String failLog;
    private Timestamp etlTime;

    @Id
    @Column(name = "rowkey", nullable = true, length = 255)
    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    @Basic
    @Column(name = "db_name", nullable = true, length = 255)
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Basic
    @Column(name = "tb_name", nullable = true, length = 255)
    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    @Basic
    @Column(name = "prov_id", nullable = true, length = 255)
    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    @Basic
    @Column(name = "date_id", nullable = true, length = 255)
    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 255)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "message", nullable = true, length = -1)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "final_status", nullable = true, length = 255)
    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    @Basic
    @Column(name = "fail_log", nullable = true, length = -1)
    public String getFailLog() {
        return failLog;
    }

    public void setFailLog(String failLog) {
        this.failLog = failLog;
    }

    @Basic
    @Column(name = "etl_time", nullable = true)
    public Timestamp getEtlTime() {
        return etlTime;
    }

    public void setEtlTime(Timestamp etlTime) {
        this.etlTime = etlTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DvBatchBoncToScheLogEntity that = (DvBatchBoncToScheLogEntity) o;
        return Objects.equals(rowkey, that.rowkey) &&
                Objects.equals(dbName, that.dbName) &&
                Objects.equals(tbName, that.tbName) &&
                Objects.equals(provId, that.provId) &&
                Objects.equals(dateId, that.dateId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(message, that.message) &&
                Objects.equals(finalStatus, that.finalStatus) &&
                Objects.equals(failLog, that.failLog) &&
                Objects.equals(etlTime, that.etlTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowkey, dbName, tbName, provId, dateId, status, message, finalStatus, failLog, etlTime);
    }
}
