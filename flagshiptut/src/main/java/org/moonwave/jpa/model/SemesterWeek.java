package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the semester_week database table.
 * 
 */
@Entity
@Table(name="semester_week")

@NamedQueries({
    @NamedQuery(name="SemesterWeek.findAll",  query="SELECT s FROM SemesterWeek s"),
    @NamedQuery(name="SemesterWeek.findById", query="SELECT s FROM SemesterWeek s WHERE s.id = :id")
})

public class SemesterWeek implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String semester;

    @Temporal(TemporalType.DATE)
    @Column(name="start_day")
    private Date startDay;

    private String week;

    public SemesterWeek() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSemester() {
        return this.semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Date getStartDay() {
        return this.startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof SemesterWeek)) {
            return false;
        }
        SemesterWeek other = (SemesterWeek) o;
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
        sb.append(",week= ").append(week);
        sb.append(",startDay= ").append(startDay);
        return sb.toString();
    }
}