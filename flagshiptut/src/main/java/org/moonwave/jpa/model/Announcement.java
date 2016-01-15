package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the announcement database table.
 * 
 */
@Entity
@Table(name="announcement")

@NamedQueries({
    @NamedQuery(name="Announcement.findAll",  query="SELECT a FROM Announcement a order by a.createTime DESC"),
    @NamedQuery(name="Announcement.findById", query="SELECT a FROM Announcement a WHERE a.id = :id"),
    @NamedQuery(name="Announcement.findByUserId", query="SELECT a FROM Announcement a WHERE a.user.id = :userId order by a.createTime DESC"),
    @NamedQuery(name="Announcement.findByUserIdInDateRange", query="SELECT a FROM Announcement a WHERE a.user.id = :userId order by a.createTime DESC")
})

public class Announcement implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String subject;

    @Lob
    private String body;

    private Boolean published;

    //bi-directional one-to-one association to User
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="create_time")
    private Timestamp createTime;

    @Column(name="update_time")
    private Timestamp updateTime;

    public Announcement() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getPublished() {
        return (published != null) ? published : false;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getBodyFirst60() {
        String bodyFirst60 = null;
        if (this.body != null) {
            bodyFirst60 = this.body.substring(0, Math.min(60, body.length()));
            if (this.body.length() > 60) {
                bodyFirst60 += "...";
            }
        }
        return bodyFirst60;
    }

    @Override
    public int hashCode() {
        return ((Integer)id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof Announcement)) {
            return false;
        }
        Announcement other = (Announcement) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",subject= ").append(subject);
        sb.append(",createTime= ").append(createTime);
        return sb.toString();
    }
}