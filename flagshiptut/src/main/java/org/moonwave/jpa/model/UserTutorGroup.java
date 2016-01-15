package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the user_tutor_group database table.
 * This class retired
 * TODO - remove 
 */
@Entity
@Table(name="user_tutor_group")
@NamedQuery(name="UserTutorGroup.findAll", query="SELECT u FROM UserTutorGroup u")
public class UserTutorGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="tutor_group_id")
    private short tutorGroupId;

    @Column(name="update_time")
    private Timestamp updateTime;

    @Column(name="user_id")
    private int userId;

    public UserTutorGroup() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getTutorGroupId() {
        return this.tutorGroupId;
    }

    public void setTutorGroupId(short tutorGroupId) {
        this.tutorGroupId = tutorGroupId;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}