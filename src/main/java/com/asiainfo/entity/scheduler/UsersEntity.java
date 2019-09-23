package com.asiainfo.entity.scheduler;

import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Classname UsersEntity
 * @Description TODO
 * @Date 2019/9/18 16:46
 * @Created by Jhon_yh
 */
@ToString
@Entity
@Component
@Table(name = "users", schema = "test", catalog = "")
public class UsersEntity {
    private int id;
    private String username;
    private String password;
    private int groupId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "group_id", nullable = false)
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id &&
                groupId == that.groupId &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, password, groupId);
    }
}
