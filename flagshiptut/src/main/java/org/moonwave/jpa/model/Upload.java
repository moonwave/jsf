package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.io.FilenameUtils;

import java.sql.Timestamp;

/**
 * The persistent class for the uploads database table.
 * 
 */
@Entity
@Table(name="upload")

@NamedQueries({
	@NamedQuery(name="Upload.findAll", query="SELECT u FROM Upload u"),
    @NamedQuery(name="Upload.findByUserAnnouncement", query="SELECT u FROM Upload u WHERE u.userId = :userId and u.announcementId = :announcementId"),
    @NamedQuery(name="Upload.findByUserGroupPost", query="SELECT u FROM Upload u WHERE u.userId = :userId and u.groupPostId = :groupPostId")
})

public class Upload implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="tutor_group_id")
    private Short tutorGroupId;

    @Column(name="announcement_id")
    private Integer announcementId;

    @Column(name="group_post_id")
    private Integer groupPostId;

    private String description;

    private String filepath;

    @Column(name="create_time")
    private Timestamp createTime;

    @Transient
    private String tag;

    public Upload() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getTutorGroupId() {
        return tutorGroupId;
    }

    public void setTutorGroupId(Short tutorGroupId) {
        this.tutorGroupId = tutorGroupId;
    }

    public Integer getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Integer announcementId) {
        this.announcementId = announcementId;
    }

    public Integer getGroupPostId() {
        return groupPostId;
    }

    public void setGroupPostId(Integer groupPostId) {
        this.groupPostId = groupPostId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof Upload)) {
            return false;
        }
        Upload other = (Upload) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",userId= ").append(userId);
        sb.append(",description= ").append(description);
        sb.append(",filepath= ").append(filepath);
        return sb.toString();
    }

    // ========================================================== Helper methods
    public String getFilename() {
        return FilenameUtils.getName(filepath);
    }
}