package org.moonwave.jpa.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")

@NamedQueries({
    @NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
    @NamedQuery(name="User.findActiveUsers", query="SELECT u FROM User u where u.active = true"),
    @NamedQuery(name="User.findActiveGenericUsers", query="SELECT u FROM User u where u.genericUser = true AND u.active = true"),
    @NamedQuery(name="User.findById",  query="SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name="User.findByLoginId",  query="SELECT u FROM User u WHERE u.loginId = :loginId"),
    @NamedQuery(name="User.findByEmail",  query="SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name="User.findByActionCode",  query="SELECT u FROM User u WHERE u.actionCode = :actionCode"),
    @NamedQuery(name="User.findInIds", query="SELECT u FROM User u WHERE u.id in (:ids)")
})

public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="chinese_name")
    private String chineseName;

    @Column(name="login_id")
    private String loginId;

    private String password;
    private String email;
    private String phone;
    private Boolean active;

    @Column(name="generic_user")
    private Boolean genericUser;

    private String hint;
    private String answer;
    private String timezone;
    private String tag;

    @Column(name="action_code")
    private String actionCode;

    @Column(name="login_attempt")
    private short loginAttempt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_attempt_ts")
    private Date lastAttemptTs;

    @Column(name="last_login_ip")
    private String lastLoginIp;

    @Column(name="update_time")
    private Timestamp updateTime;

    @Column(name="create_time")
    private Timestamp createTime;

    //bi-directional many-to-many association to Role
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="user_role"
        , joinColumns={
            @JoinColumn(name="user_id")
            }
        , inverseJoinColumns={
            @JoinColumn(name="role_id")
            }
        )
    private List<Role> roles;

    //bi-directional many-to-many association to TutorGroup
//    @ManyToMany(cascade=CascadeType.ALL)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="user_tutor_group"
        , joinColumns={
            @JoinColumn(name="user_id")
            }
        , inverseJoinColumns={
            @JoinColumn(name="tutor_group_id")
            }
        )
    private List<TutorGroup> tutorGroups;

    //bi-directional one-to-one association to Announcement
    @OneToOne(mappedBy="user", fetch = FetchType.EAGER)
    private Announcement announcement;

    //bi-directional one-to-one association to GroupPost
    @OneToOne(mappedBy="user", fetch = FetchType.LAZY)
    private GroupPost groupPost;

    //bi-directional one-to-one association to EvaluationPerformance
    @OneToOne(mappedBy="user", fetch = FetchType.EAGER)
    private EvaluationPerformance user;

    //bi-directional one-to-one association to EvaluationPerformance
    @OneToOne(mappedBy="tutor", fetch = FetchType.EAGER)
    private EvaluationPerformance tutor;

    //bi-directional one-to-one association to EvaluationPerformance
    @OneToOne(mappedBy="user", fetch = FetchType.EAGER)
    private EvaluationObjective user2;

    //bi-directional one-to-one association to EvaluationPerformance
    @OneToOne(mappedBy="tutor", fetch = FetchType.EAGER)
    private EvaluationObjective tutor2;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getGenericUser() {
        return genericUser;
    }

    public void setGenericUser(Boolean genericUser) {
        this.genericUser = genericUser;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public short getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(short loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public Date getLastAttemptTs() {
        return lastAttemptTs;
    }

    public void setLastAttemptTs(Date lastAttemptTs) {
        this.lastAttemptTs = lastAttemptTs;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<TutorGroup> getTutorGroups() {
        return this.tutorGroups;
    }

    public void setTutorGroups(List<TutorGroup> tutorGroups) {
        this.tutorGroups = tutorGroups;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public GroupPost getGroupPost() {
        return groupPost;
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
        if ((o == null) || !(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id= ").append(id);
        sb.append(",firstname= ").append(firstName);
        sb.append(",lastname= ").append(lastName);
        sb.append(",loginid= ").append(loginId);
        sb.append(",timezone= ").append(timezone);
        return sb.toString();
    }

    // ========================================================== Helper methods

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public int compareTo(User user2) {
        return this.getName().toLowerCase().compareTo(user2.getName().toLowerCase());
    }

    public void clearFields() {
        this.firstName = null;
        this.lastName = null;
        this.loginId = null;
        this.password = null;
        this.hint = null;
    }

}