package org.moonwave.view.content.add;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.EvaluationPerformanceBO;
import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.EvaluationPerformance;
import org.moonwave.util.StringUtil;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performance List View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class PerformanceListView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(PerformanceListView.class);

    private List<EvaluationPerformance> data;
    private EvaluationPerformance current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @PostConstruct
    public void init() {
        data = new EvaluationPerformanceBO().findByTutorId(super.getLoggedInUser().getId());
    }

    public List<EvaluationPerformance> getData() {
        return data;
    }

    public void setData(List<EvaluationPerformance> data) {
        this.data = data;
    }

    public EvaluationPerformance getCurrent() {
        return current;
    }

    public void setCurrent(EvaluationPerformance current) {
        this.current = current;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<EvaluationPerformance> bo = new GenericBO<>(EvaluationPerformance.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
        }
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    // ========================================================== ActionListener

    public void newPerformance() throws IOException {
        super.redirectTo("/content/add/performance.xhtml");
    }

    public void edit(String selectedId) throws IOException {
        super.redirectTo("/content/add/performance.xhtml?selectedId=" + selectedId);
    }

    /**
    *
    * @param selectedId selected Announcement id
    */
    public void togglePublish(String selectedId) {
       if (!StringUtil.nullOrEmpty(selectedId)) {
           GenericBO<EvaluationPerformance> bo = new GenericBO<>(EvaluationPerformance.class);
           this.current = bo.findById(Integer.parseInt(selectedId));
           this.current.setPublished(!this.current.getPublished());
           super.getBasebo().update(current);

           data = new EvaluationPerformanceBO().findByTutorId(super.getLoggedInUser().getId());
       }
    }

    public void delete(String selectedId) throws IOException {
        try {
            GenericBO<EvaluationPerformance> bo = new GenericBO<>(EvaluationPerformance.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            super.getBasebo().remove(current);

            // show successful message and reset fields
            super.info("Data deletion successful");

        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
        super.redirectTo("/content/add/performanceList.xhtml");
    }
}