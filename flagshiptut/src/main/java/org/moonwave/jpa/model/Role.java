package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")

@NamedQueries({
    @NamedQuery(name="Role.findAll",  query="SELECT r FROM Role r"),
    @NamedQuery(name="Role.findById", query="SELECT r FROM Role r WHERE r.id = :id"),
    @NamedQuery(name="Role.findStudents", query="SELECT r FROM Role r WHERE r.alias = 'student'"),
    @NamedQuery(name="Role.findTutors", query="SELECT r FROM Role r WHERE r.alias = 'tutor'")
})

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Short id;

    private String alias;

    private String name;

    private String privileges;

    //bi-directional many-to-many association to User
    @ManyToMany(mappedBy="roles", fetch = FetchType.EAGER)
    private List<User> users;

    public Role() {
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivileges() {
        return this.privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof Role)) {
            return false;
        }
        Role other = (Role) o;
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