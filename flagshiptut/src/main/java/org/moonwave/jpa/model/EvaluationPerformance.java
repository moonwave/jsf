package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the evaluation_performance database table.
 * 
 */
@Entity
@Table(name="evaluation_performance")

@NamedQueries({
    @NamedQuery(name="EvaluationPerformance.findAll",  query="SELECT e FROM EvaluationPerformance e"),
    @NamedQuery(name="EvaluationPerformance.findById", query="SELECT e FROM EvaluationPerformance e WHERE e.id = :id"),
    @NamedQuery(name="EvaluationPerformance.findByUserId", query="SELECT e FROM EvaluationPerformance e WHERE e.user.id = :userId order by e.createTime desc"),
    @NamedQuery(name="EvaluationPerformance.findByTutorId", query="SELECT e FROM EvaluationPerformance e WHERE e.tutor.id = :tutorId order by e.createTime desc")
})

public class EvaluationPerformance implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    //bi-directional one-to-one association to User
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    //bi-directional one-to-one association to User
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tutor_id")
    private User tutor;

    private String semester;

    private String week;

    private short attendance;

    private short participation;

    private short performance;

    private short total;

    private String note;

    private Boolean published;

    @Column(name="update_time")
    private Timestamp updateTime;

    @Column(name="create_time")
    private Timestamp createTime;

    public EvaluationPerformance() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        if ((user == null) && (id == null)) {
            user = new User();
        }
        return this.user;
    }

    public void setUser(User user) {
        this.user= user;
    }

    public User getTutor() {
        if ((tutor == null) && (id == null)) {
            tutor = new User();
        }
        return tutor;
    }

    public void setTutor(User tutor) {
        this.tutor = tutor;
    }

    public String getSemester() {
        return this.semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public short getAttendance() {
        return this.attendance;
    }

    public void setAttendance(short attendance) {
        this.attendance = attendance;
    }

    public short getParticipation() {
        return this.participation;
    }

    public void setParticipation(short participation) {
        this.participation = participation;
    }

    public short getPerformance() {
        return this.performance;
    }

    public void setPerformance(short performance) {
        this.performance = performance;
    }

    public short getTotal() {
        return this.total;
    }

    public void setTotal(short toatl) {
        this.total = toatl;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getPublished() {
        return (published != null) ? published : false;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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
        if ((o == null) || !(o instanceof EvaluationPerformance)) {
            return false;
        }
        EvaluationPerformance other = (EvaluationPerformance) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",user_id= ").append(user.getId());
        sb.append(",tutor_id= ").append(tutor.getId());
        sb.append(",semester= ").append(semester);
        sb.append(",week= ").append(week);
        sb.append(",attendance= ").append(attendance);
        sb.append(",participation= ").append(participation);
        sb.append(",performance= ").append(performance);
        sb.append(",total= ").append(total);
        return sb.toString();
    }

}