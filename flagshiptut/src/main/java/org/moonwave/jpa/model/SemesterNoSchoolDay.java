package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the semester_no_school_day database table.
 * 
 */
@Entity
@Table(name="semester_no_school_day")

@NamedQueries({
    @NamedQuery(name="SemesterNoSchoolDay.findAll",  query="SELECT s FROM SemesterNoSchoolDay s"),
    @NamedQuery(name="SemesterNoSchoolDay.findById", query="SELECT w FROM SemesterNoSchoolDay w WHERE w.id = :id")
})

public class SemesterNoSchoolDay implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name="no_school_day")
    private Date noSchoolDay;

    private String semester;

    public SemesterNoSchoolDay() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getNoSchoolDay() {
        return this.noSchoolDay;
    }

    public void setNoSchoolDay(Date noSchoolDay) {
        this.noSchoolDay = noSchoolDay;
    }

    public String getSemester() {
        return this.semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof SemesterNoSchoolDay)) {
            return false;
        }
        SemesterNoSchoolDay other = (SemesterNoSchoolDay) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",semester= ").append(semester);
        sb.append(",noSchoolDay= ").append(noSchoolDay);
        return sb.toString();
    }
}