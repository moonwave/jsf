package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the semester database table.
 * 
 */
@Entity
@Table(name="semester")

@NamedQueries({
    @NamedQuery(name="Semester.findAll",  query="SELECT s FROM Semester s order by s.id"),
    @NamedQuery(name="Semester.findById", query="SELECT s FROM Semester s WHERE s.id = :id")
})

public class Semester implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Short id;

    private String alias;

    @Temporal(TemporalType.DATE)
    @Column(name="end_date")
    private Date endDate;

    private String name;

    @Column(name="number_of_weeks")
    private short numberOfWeeks;

    @Temporal(TemporalType.DATE)
    @Column(name="start_date")
    private Date startDate;

    @Column(name="start_weekday")
    private String startWeekday;

    public Semester() {
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getNumberOfWeeks() {
        return this.numberOfWeeks;
    }

    public void setNumberOfWeeks(short numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartWeekday() {
        return this.startWeekday;
    }

    public void setStartWeekday(String startWeekday) {
        this.startWeekday = startWeekday;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof Semester)) {
            return false;
        }
        Semester other = (Semester) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",alias= ").append(alias);
        sb.append(",name= ").append(name);
        return sb.toString();
    }
}