package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the CourseReg_reg database table.
 * 
 */
@Entity
@Table(name="Course_reg")

@NamedQueries({
    @NamedQuery(name="CourseReg.findAll", query="SELECT c FROM CourseReg c"),
    @NamedQuery(name="CourseReg.findById", query="SELECT c FROM CourseReg c WHERE c.id = :id")
})

public class CourseReg implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="CourseReg_id")
    private short CourseRegId;

    @Column(name="create_time")
    private Timestamp createTime;

    @Temporal(TemporalType.DATE)
    @Column(name="reg_date")
    private Date regDate;

    @Column(name="student_id")
    private int studentId;

    @Column(name="update_time")
    private Timestamp updateTime;

    public CourseReg() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getCourseRegId() {
        return this.CourseRegId;
    }

    public void setCourseRegId(short CourseRegId) {
        this.CourseRegId = CourseRegId;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Date getRegDate() {
        return this.regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public int getStudentId() {
        return this.studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof CourseReg)) {
            return false;
        }
        CourseReg other = (CourseReg) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",CourseRegId= ").append(CourseRegId);
        return sb.toString();
    }
}