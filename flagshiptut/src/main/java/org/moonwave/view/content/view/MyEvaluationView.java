package org.moonwave.view.content.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.EvaluationObjectiveBO;
import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.EvaluationObjective;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * My Evaluation View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class MyEvaluationView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(MyEvaluationView.class);

    private List<EvaluationObjective> data;
    private EvaluationObjective current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @PostConstruct
    public void init() {
        data = new EvaluationObjectiveBO().findByUserId(super.getLoggedInUser().getId());
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

    // ========================================================= Private methods
}