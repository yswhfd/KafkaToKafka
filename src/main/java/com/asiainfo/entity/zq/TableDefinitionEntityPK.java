package com.asiainfo.entity.zq;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Classname TableDefinitionEntityPK
 * @Description TODO
 * @Date 2019/9/19 14:30
 * @Created by Jhon_yh
 */
public class TableDefinitionEntityPK implements Serializable {
    private String tableid;
    private String tablename;

    @Column(name = "TABLEID", nullable = false, length = 128)
    @Id
    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    @Column(name = "TABLENAME", nullable = false, length = 255)
    @Id
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDefinitionEntityPK that = (TableDefinitionEntityPK) o;
        return Objects.equals(tableid, that.tableid) &&
                Objects.equals(tablename, that.tablename);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tableid, tablename);
    }
}
