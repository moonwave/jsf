package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the group_post_to_group database table.
 * 
 */
@Entity
@Table(name="group_post_to_group")

@NamedQueries({
    @NamedQuery(name="GroupPostToGroup.findAll",  query="SELECT g FROM GroupPostToGroup g"),
    @NamedQuery(name="GroupPostToGroup.findById", query="SELECT g FROM GroupPostToGroup g WHERE g.id = :id")
})

public class GroupPostToGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="tutor_group_id")
    private short tutorGroupId;

    //bi-directional many-to-one association to GroupPost
    @ManyToOne
    @JoinColumn(name="group_post_id")
    private GroupPost groupPost;

    public GroupPostToGroup() {
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

    public GroupPost getGroupPost() {
        return this.groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof GroupPostToGroup)) {
            return false;
        }
        GroupPostToGroup other = (GroupPostToGroup) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",tutorGroupId= ").append(tutorGroupId);
        return sb.toString();
    }
}