package com.asiainfo.entity.scheduler;

import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Classname ProvTableEntity
 * @Description TODO
 * @Date 2019/9/18 11:35
 * @Created by Jhon_yh
 */
@ToString
@Component
@DynamicUpdate
@Entity
@Table(name = "prov_table", schema = "test")
public class ProvTableEntity {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
//    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "prov_id", nullable = false, length = 25)
    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    @Basic
    @Column(name = "prov_name", nullable = true, length = 255)
    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    @Basic
    @Column(name = "src_flag", nullable = true, length = 4)
    public String getSrcFlag() {
        return srcFlag;
    }

    public void setSrcFlag(String srcFlag) {
        this.srcFlag = srcFlag;
    }

    @Basic
    @Column(name = "tar_flag", nullable = true, length = 4)
    public String getTarFlag() {
        return tarFlag;
    }

    public void setTarFlag(String tarFlag) {
        this.tarFlag = tarFlag;
    }

    @Basic
    @Column(name = "tar_db_name", nullable = true, length = 255)
    public String getTarDbName() {
        return tarDbName;
    }

    public void setTarDbName(String tarDbName) {
        this.tarDbName = tarDbName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvTableEntity that = (ProvTableEntity) o;
        return Objects.equals(provId, that.provId) &&
                Objects.equals(provName, that.provName) &&
                Objects.equals(srcFlag, that.srcFlag) &&
                Objects.equals(tarFlag, that.tarFlag) &&
                Objects.equals(tarDbName, that.tarDbName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(provId, provName, srcFlag, tarFlag, tarDbName);
    }

    private String provId;
    private String provName;
    private String srcFlag;
    private String tarFlag;
    private String tarDbName;

}
