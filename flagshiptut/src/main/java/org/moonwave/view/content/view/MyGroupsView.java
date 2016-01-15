package org.moonwave.view.content.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.TutorGroup;
import org.moonwave.jpa.model.User;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * My Groups View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class MyGroupsView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(MyGroupsView.class);

    private List<TutorGroup> data;
    private TutorGroup current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @PostConstruct
    public void init() {
        User user = getLoggedInUser();
        data = user.getTutorGroups();
    }

    public List<TutorGroup> getData() {
        return data;
    }

    public void setData(List<TutorGroup> data) {
        this.data = data;
    }

    public TutorGroup getCurrent() {
        return current;
    }

    public void setCurrent(TutorGroup current) {
        this.current = current;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<TutorGroup> bo = new GenericBO<>(TutorGroup.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
        }
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    // ========================================================= Private methods

}