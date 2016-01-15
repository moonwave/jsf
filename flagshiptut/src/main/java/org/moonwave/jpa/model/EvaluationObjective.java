package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the evaluation_objective database table.
 * 
 */
@Entity
@Table(name="evaluation_objective")

@NamedQueries({
    @NamedQuery(name="EvaluationObjective.findAll",  query="SELECT e FROM EvaluationObjective e"),
    @NamedQuery(name="EvaluationObjective.findById", query="SELECT e FROM EvaluationObjective e WHERE e.id = :id"),
    @NamedQuery(name="EvaluationObjective.findByUserId", query="SELECT e FROM EvaluationObjective e WHERE e.user.id = :userId order by e.createTime desc"),
    @NamedQuery(name="EvaluationObjective.findByTutorId", query="SELECT e FROM EvaluationObjective e WHERE e.tutor.id = :tutorId order by e.createTime desc"),
    @NamedQuery(name="EvaluationObjective.findByParentEvaluationId", query="SELECT e FROM EvaluationObjective e WHERE e.parentEvaluationId = :parentEvaluationId")
})

public class EvaluationObjective implements Serializable {
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

    @Column(name="eval_part1_1")
    private Boolean evalPart11;

    @Column(name="eval_part1_2")
    private Boolean evalPart12;

    @Column(name="eval_part1_3")
    private Boolean evalPart13;

    @Column(name="eval_part1_comments")
    private String evalPart1Comments;

    @Column(name="eval_part2_1")
    private Boolean evalPart21;

    @Column(name="eval_part2_2")
    private Boolean evalPart22;

    @Column(name="eval_part2_3")
    private Boolean evalPart23;

    @Column(name="eval_part2_comments")
    private String evalPart2Comments;

    @Column(name="eval_part3_1")
    private Boolean evalPart31;

    @Column(name="eval_part3_2")
    private Boolean evalPart32;

    @Column(name="eval_part3_3")
    private Boolean evalPart33;

    @Column(name="eval_part3_comments")
    private String evalPart3Comments;

    @Column(name="student_evaluation")
    private Boolean studentEvaluation;

    private Boolean published;

    // self evaluation is the parent of a tutor evaluation
    @Column(name="parent_evaluation_id")
    private Integer parentEvaluationId;

    @Column(name="update_time")
    private Timestamp updateTime;

    @Column(name="create_time")
    private Timestamp createTime;

    // helper fields
    @Transient
    EvaluationObjective selfEvaluation;

    @Transient
    EvaluationObjective tutorEvaluation;

    public EvaluationObjective() {
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

    public Boolean getEvalPart11() {
        return this.evalPart11;
    }

    public void setEvalPart11(Boolean evalPart11) {
        this.evalPart11 = evalPart11;
    }

    public Boolean getEvalPart12() {
        return this.evalPart12;
    }

    public void setEvalPart12(Boolean evalPart12) {
        this.evalPart12 = evalPart12;
    }

    public Boolean getEvalPart13() {
        return this.evalPart13;
    }

    public void setEvalPart13(Boolean evalPart13) {
        this.evalPart13 = evalPart13;
    }

    public String getEvalPart1Comments() {
        return this.evalPart1Comments;
    }

    public void setEvalPart1Comments(String evalPart1Comments) {
        this.evalPart1Comments = evalPart1Comments;
    }

    public Boolean getEvalPart21() {
        return this.evalPart21;
    }

    public void setEvalPart21(Boolean evalPart21) {
        this.evalPart21 = evalPart21;
    }

    public Boolean getEvalPart22() {
        return this.evalPart22;
    }

    public void setEvalPart22(Boolean evalPart22) {
        this.evalPart22 = evalPart22;
    }

    public Boolean getEvalPart23() {
        return this.evalPart23;
    }

    public void setEvalPart23(Boolean evalPart23) {
        this.evalPart23 = evalPart23;
    }

    public String getEvalPart2Comments() {
        return this.evalPart2Comments;
    }

    public void setEvalPart2Comments(String evalPart2Comments) {
        this.evalPart2Comments = evalPart2Comments;
    }

    public Boolean getEvalPart31() {
        return this.evalPart31;
    }

    public void setEvalPart31(Boolean evalPart31) {
        this.evalPart31 = evalPart31;
    }

    public Boolean getEvalPart32() {
        return this.evalPart32;
    }

    public void setEvalPart32(Boolean evalPart32) {
        this.evalPart32 = evalPart32;
    }

    public Boolean getEvalPart33() {
        return this.evalPart33;
    }

    public void setEvalPart33(Boolean evalPart33) {
        this.evalPart33 = evalPart33;
    }

    public String getEvalPart3Comments() {
        return this.evalPart3Comments;
    }

    public void setEvalPart3Comments(String evalPart3Comments) {
        this.evalPart3Comments = evalPart3Comments;
    }

    public Boolean getStudentEvaluation() {
        return this.studentEvaluation;
//        return (parentEvaluationId != null) ? true : false;
    }

    public void setStudentEvaluation(Boolean studentEvaluation) {
        // ignore for now
        this.studentEvaluation = studentEvaluation;
    }

    public Boolean getPublished() {
        return (published != null) ? published : false;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Integer getParentEvaluationId() {
        return parentEvaluationId;
    }

    public void setParentEvaluationId(Integer parentEvaluationId) {
        this.parentEvaluationId = parentEvaluationId;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof EvaluationObjective)) {
            return false;
        }
        EvaluationObjective other = (EvaluationObjective) o;
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
        sb.append(",studentEvaluation= ").append(studentEvaluation);
        return sb.toString();
    }

    // ========================================================== Helper methods

    public EvaluationObjective getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(EvaluationObjective selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public EvaluationObjective getTutorEvaluation() {
        return tutorEvaluation;
    }

    public void setTutorEvaluation(EvaluationObjective tutorEvaluation) {
        this.tutorEvaluation = tutorEvaluation;
    }

}