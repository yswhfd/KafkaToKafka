package com.asiainfo.entity.zq;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Classname TableDefinitionEntity
 * @Description TODO
 * @Date 2019/9/19 14:30
 * @Created by Jhon_yh
 */
@Entity
@Table(name = "table_definition", schema = "test")
@Component
@IdClass(TableDefinitionEntityPK.class)
public class TableDefinitionEntity {
    private String tableid;
    private String tablename;
    private String tenants;
    private String tableowner;
    private String description;
    private Integer datasource;
    private Integer warn;
    private String plantime;

    @Id
    @Column(name = "TABLEID", nullable = false, length = 128)
    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    @Id
    @Column(name = "TABLENAME", nullable = false, length = 255)
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    @Basic
    @Column(name = "TENANTS", nullable = true, length = 60)
    public String getTenants() {
        return tenants;
    }

    public void setTenants(String tenants) {
        this.tenants = tenants;
    }

    @Basic
    @Column(name = "TABLEOWNER", nullable = true, length = 128)
    public String getTableowner() {
        return tableowner;
    }

    public void setTableowner(String tableowner) {
        this.tableowner = tableowner;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 256)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "DATASOURCE", nullable = true)
    public Integer getDatasource() {
        return datasource;
    }

    public void setDatasource(Integer datasource) {
        this.datasource = datasource;
    }

    @Basic
    @Column(name = "WARN", nullable = true)
    public Integer getWarn() {
        return warn;
    }

    public void setWarn(Integer warn) {
        this.warn = warn;
    }

    @Basic
    @Column(name = "PLANTIME", nullable = true, length = -1)
    public String getPlantime() {
        return plantime;
    }

    public void setPlantime(String plantime) {
        this.plantime = plantime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDefinitionEntity that = (TableDefinitionEntity) o;
        return Objects.equals(tableid, that.tableid) &&
                Objects.equals(tablename, that.tablename) &&
                Objects.equals(tenants, that.tenants) &&
                Objects.equals(tableowner, that.tableowner) &&
                Objects.equals(description, that.description) &&
                Objects.equals(datasource, that.datasource) &&
                Objects.equals(warn, that.warn) &&
                Objects.equals(plantime, that.plantime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tableid, tablename, tenants, tableowner, description, datasource, warn, plantime);
    }
}
