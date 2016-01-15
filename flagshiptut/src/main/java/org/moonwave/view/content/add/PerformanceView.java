package org.moonwave.view.content.add;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.EvaluationPerformance;
import org.moonwave.jpa.model.Semester;
import org.moonwave.jpa.model.User;
import org.moonwave.jpa.model.Week;
import org.moonwave.util.StackTrace;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SelfEvaluation View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class PerformanceView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(PerformanceView.class);

    EvaluationPerformance current;
    List<User> students;
    List<User> tutors;
    List<Semester> semesters;
    List<Week> weeks;

    @PostConstruct
    public void init() {
        String selectedId = super.getParameter("selectedId");
        if (selectedId != null) { // edit
            GenericBO<EvaluationPerformance> bo = new GenericBO<>(EvaluationPerformance.class);
            current = bo.findById(Integer.valueOf(selectedId));
        } else {
            current = new EvaluationPerformance();
            current.setPublished(true);
            current.setTutor(super.getLoggedInUser());
        }

        // get a list of students under the tutor
        students = new UserBO().findAllStudents();
        Collections.sort(students);

        // get a list of tutors
        tutors = new UserBO().findAllTutors();
        Collections.sort(tutors);

        GenericBO<Semester> bo = new GenericBO<>(Semester.class);
        semesters = bo.findAll();

        GenericBO<Week> weekbo = new GenericBO<>(Week.class);
        weeks = weekbo.findAll();

    }

    public EvaluationPerformance getCurrent() {
        return current;
    }

    public void setCurrent(EvaluationPerformance current) {
        this.current = current;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public List<User> getTutors() {
        return tutors;
    }

    public void setTutors(List<User> tutors) {
        this.tutors = tutors;
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    // ========================================================== ActionListener

    public void cancel() throws IOException {
        redirectToListView();
    }

    public void redirectToListView() throws IOException {
        super.redirectTo("/content/add/performanceList.xhtml");
    }

    public String save() {
        // validation
//        if (StringUtil.nullOrEmpty(subject)) {
//            super.error("Subject is empty");
//            return null;
//        }
        try {
            if (current.getId() == null) {
                current.setCreateTime(super.getSqlTimestamp());
                super.getBasebo().persist(current);
            } else {
                super.getBasebo().update(current);
            }

            super.info("Data was saved successfully");
            redirectToListView();
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error(StackTrace.toString(e));
        }
        return null;
    }
}
