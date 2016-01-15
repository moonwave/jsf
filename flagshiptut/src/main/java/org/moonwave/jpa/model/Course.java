package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the course database table.
 * 
 */
@Entity
@Table(name="course")

@NamedQueries({
    @NamedQuery(name="Course.findAll",  query="SELECT c FROM Course c"),
    @NamedQuery(name="Course.findById", query="SELECT c FROM Course c WHERE c.id = :id")
})

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Short id;

    @Column(name="create_time")
    private Timestamp createTime;

    @Lob
    private String description;

    private String name;

    @Column(name="update_time")
    private Timestamp updateTime;

    public Course() {
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
        if ((o == null) || !(o instanceof Course)) {
            return false;
        }
        Course other = (Course) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",name= ").append(name);
        return sb.toString();
    }
}