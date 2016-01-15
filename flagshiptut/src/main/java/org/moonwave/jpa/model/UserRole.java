package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the user_role database table.
 * This class retired
 * TODO - remove 
 * 
 */
@Entity
@Table(name="user_role")

@NamedQueries({
    @NamedQuery(name="UserRole.findAll", query="SELECT u FROM UserRole u"),
    @NamedQuery(name="UserRole.findByRole", query = "SELECT u FROM UserRole u WHERE u.roleId = :roleId"),
    @NamedQuery(name="UserRole.findByRoleUser", query = "SELECT u FROM UserRole u WHERE u.roleId = :roleId AND u.userId = :userId")
})

public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private int userId;

    @Column(name="role_id")
    private short roleId;

    @Column(name="update_time")
    private Timestamp updateTime;

    public UserRole() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public short getRoleId() {
        return this.roleId;
    }

    public void setRoleId(short roleId) {
        this.roleId = roleId;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}