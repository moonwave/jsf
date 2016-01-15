package org.moonwave.view.content.add;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.EvaluationObjectiveBO;
import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.EvaluationObjective;
import org.moonwave.util.JSFUtil;
import org.moonwave.util.StringUtil;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Evaluation List View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class EvaluationListView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(EvaluationListView.class);

    private List<EvaluationObjective> data;
    private EvaluationObjective current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    boolean studentAccess;
    boolean tutorAccess;

    // helper fields
    String title;

    @PostConstruct
    public void init() {
        Map<String, String> map = JSFUtil.getRequestParameterMap();
        studentAccess = !StringUtil.nullOrEmpty(map.get("student")) && map.get("student").equals("true") ? true : false;
        tutorAccess = !StringUtil.nullOrEmpty(map.get("tutor")) && map.get("tutor").equals("true") ? true : false;

        if (studentAccess) {
            title = super.getLocaleLabels().getString("selfEvaluation");
            data = new EvaluationObjectiveBO().findByUserId(super.getLoggedInUser().getId());
        } else {
            title = super.getLocaleLabels().getString("tutorEvaluation");
            data = new EvaluationObjectiveBO().findByTutorId(super.getLoggedInUser().getId());
        }
    }

    public List<EvaluationObjective> getData() {
        return data;
    }

    public void setData(List<EvaluationObjective> data) {
        this.data = data;
    }

    public EvaluationObjective getCurrent() {
        return current;
    }

    public void setCurrent(EvaluationObjective current) {
        this.current = current;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<EvaluationObjective> bo = new GenericBO<>(EvaluationObjective.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
        }
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    public boolean isStudentAccess() {
        return studentAccess;
    }

    public boolean isTutorAccess() {
        return tutorAccess;
    }

    // ========================================================== ActionListener

    public void newSelfEvaluation() throws IOException {
        super.redirectTo("/content/add/selfEvaluation.xhtml?student=true");
    }

    public void newTutorEvaluation() throws IOException {
        super.redirectTo("/content/add/selfEvaluation.xhtml?tutor=true");
    }

    /**
    *
    * @param selectedId selected Announcement id
    */
    public void togglePublish(String selectedId) {
       if (!StringUtil.nullOrEmpty(selectedId)) {
           GenericBO<EvaluationObjective> bo = new GenericBO<>(EvaluationObjective.class);
           this.current = bo.findById(Integer.parseInt(selectedId));
           this.current.setPublished(!this.current.getPublished());
           super.getBasebo().update(current);

           if (studentAccess) {
               data = new EvaluationObjectiveBO().findByUserId(super.getLoggedInUser().getId());
           } else {
               data = new EvaluationObjectiveBO().findByTutorId(super.getLoggedInUser().getId());
           }
       }
    }

    /**
    *
    * @param selectedId selected EvaluationObjective id
    */
    public void edit(String selectedId) throws IOException {
        GenericBO<EvaluationObjective> bo = new GenericBO<>(EvaluationObjective.class);
        this.current = bo.findById(Integer.parseInt(selectedId));
        if (current.getStudentEvaluation()) {
            super.redirectTo("/content/add/selfEvaluation.xhtml?student=true&selectedId=" + selectedId);
        } else {
            super.redirectTo("/content/add/selfEvaluation.xhtml?tutor=true&selectedId=" + selectedId);
        }
    }

    public void delete(String selectedId) throws IOException {
        try {
            GenericBO<EvaluationObjective> bo = new GenericBO<>(EvaluationObjective.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            super.getBasebo().remove(current);

            super.info("Data deletion successful");
            if (current.getStudentEvaluation()) {
                super.redirectTo("/content/add/evaluationList.xhtml?student=true");
            } else {
                super.redirectTo("/content/add/evaluationList.xhtml?tutor=true");
            }
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
    }

    // ========================================================== Helper methods

    public String getTitle() {
        return title;
    }
}