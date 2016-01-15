package org.moonwave.view.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.moonwave.jpa.bo.TutorGroupBO;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.TutorGroup;
import org.moonwave.jpa.model.User;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User TutorGroup View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class UserTutorGroupView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(UserTutorGroupView.class);

    private String selectedTutorGroupId;
    private List<TutorGroup> tutorGroups;

    private String selectedUserId;
    private List<User> users;
    private List<User> usersInTutorGroup;

    @PostConstruct
    public void init() {

        tutorGroups = new TutorGroupBO().findAllGroups();
        users = new UserBO().findActiveGenericUsers();
        // get a list of users for the first role
        if (!tutorGroups.isEmpty()) {
            selectedTutorGroupId = String.valueOf(tutorGroups.get(0).getId());
            getUsersByTutorGroupId(selectedTutorGroupId);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectedTutorGroupId() {
        return selectedTutorGroupId;
    }

    public void setSelectedTutorGroupId(String selectedTutorGroupId) {
        this.selectedTutorGroupId = selectedTutorGroupId;
    }

    public List<TutorGroup> getTutorGroups() {
        return tutorGroups;
    }

    public List<User> getUsersInTutorGroup() {
        return usersInTutorGroup;
    }

    /**
     * Handle TutorGroup change event
     * Search user_tutor_group table by selected tutor group id
     *
     * @param event
     */
    public void changeTutorGroup(AjaxBehaviorEvent event) {
        getUsersByTutorGroupId(selectedTutorGroupId);
    }

    /**
     * Handle removing user from a selected tutor group
     * or
     * public String removeUserFromTutorGroup()
     * @return
     */
    public void removeUserFromTutorGroup() {
        try {
            TutorGroup tg = new TutorGroupBO().findById(Short.parseShort(selectedTutorGroupId));
            User user = new UserBO().findById(Integer.parseInt(selectedUserId));
            List<User> users = (tg.getUsers() != null) ? tg.getUsers() : new ArrayList<User>();
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    List<TutorGroup> tutorGroups = (user.getTutorGroups() != null) ? user.getTutorGroups() : new ArrayList<TutorGroup>();
                    tutorGroups.remove(tg);
                    user.setTutorGroups(tutorGroups);
                    super.getBasebo().update(user);
                    break;
                }
            }

            getUsersByTutorGroupId(selectedTutorGroupId);
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
    }

    /**
     * Handle adding user to a selected tutor group
     * or
     * public void addUserToTutorGroup()
     * @return
     */
    public String addUserToTutorGroup() {
        try {
            TutorGroup tg = new TutorGroupBO().findById(Short.parseShort(selectedTutorGroupId));
            List<User> users = (tg.getUsers() != null) ? tg.getUsers() : new ArrayList<User>();
            for (User user : users) {
                if (user.getId() == Integer.parseInt(selectedUserId)) {
                  super.info("User is already in selected Tutor Group");
                  return null;
                }
            }
            User user = new UserBO().findById(Integer.parseInt(selectedUserId));
            List<TutorGroup> tutorGroups = (user.getTutorGroups() != null) ? user.getTutorGroups() : new ArrayList<TutorGroup>();  
            tutorGroups.add(tg);
            user.setTutorGroups(tutorGroups);
            super.getBasebo().update(user);

            getUsersByTutorGroupId(selectedTutorGroupId);
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
        return "";
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Find a list of users by selected tutor group id
     *
     * @param selectedTutorGroupId
     */
    private void getUsersByTutorGroupId(String selectedTutorGroupId) {
        try {
            TutorGroup role = new TutorGroupBO().findById(Short.parseShort(selectedTutorGroupId));
            usersInTutorGroup = role.getUsers();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}