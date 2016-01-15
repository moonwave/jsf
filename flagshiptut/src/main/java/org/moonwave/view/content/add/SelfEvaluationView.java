package org.moonwave.view.content.add;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.EvaluationObjective;
import org.moonwave.jpa.model.Semester;
import org.moonwave.jpa.model.User;
import org.moonwave.jpa.model.Week;
import org.moonwave.util.StackTrace;
import org.moonwave.view.AccessController;
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
public class SelfEvaluationView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(SelfEvaluationView.class);

    EvaluationObjective current;
    List<User> students;
    List<User> tutors;
    List<Semester> semesters;
    List<Week> weeks;

    boolean studentAccess = false; // evaluation by student 
    boolean tutorAccess = false;   // evaluation by tutor

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    // helper fields
    String title;

    @PostConstruct
    public void init() {

        studentAccess = (super.getParameter("student") != null) ? true : false;
        tutorAccess = (super.getParameter("tutor") != null) ? true : false;

        if (studentAccess) {
            title = super.getLocaleLabels().getString("selfEvaluation");
        } else {
            title = super.getLocaleLabels().getString("tutorEvaluation");
        }
        String selectedId = super.getParameter("selectedId");
        if (selectedId != null) { // edit
            GenericBO<EvaluationObjective> bo = new GenericBO<>(EvaluationObjective.class);
            current = bo.findById(Integer.valueOf(selectedId));
        } else {
            current = new EvaluationObjective();
            current.setPublished(true);
            if (studentAccess) {
                current.setUser(super.getLoggedInUser());
            }
            if (tutorAccess) {
                current.setTutor(super.getLoggedInUser());
            }
        }
        // get a list of students
        students = new UserBO().findAllStudents();
        Collections.sort(students);

        // get a list of tutors
        tutors = new UserBO().findAllTutors();
        Collections.sort(tutors);

        GenericBO<Semester> bo = new GenericBO<>(Semester.class);
        semesters = bo.findAll();

        GenericBO<Week> weekbo = new GenericBO<>(Week.class);
        weeks = weekbo.findAll();

        if (tutorAccess && !accessController.isEditor()) {
            super.accessDenied();
        }
    }

    public EvaluationObjective getCurrent() {
        return current;
    }

    public void setCurrent(EvaluationObjective Current) {
        this.current = Current;
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

    public void cancel() throws IOException {
        redirectToListView();
    }

    public void redirectToListView() throws IOException {
        if (studentAccess) {
            super.redirectTo("/content/add/evaluationList.xhtml?student=true");
        } else {
            super.redirectTo("/content/add/evaluationList.xhtml?tutor=true");
        }
    }

    public String save() {
        // validation
        try {
            if (current.getId() == null) {
                if (this.studentAccess) {
                    current.setStudentEvaluation(true);
                } else if (this.tutorAccess) {
                    current.setStudentEvaluation(false);
                }
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

    // ========================================================= Helper methods

    public String getTitle() {
        return title;
    }

}
