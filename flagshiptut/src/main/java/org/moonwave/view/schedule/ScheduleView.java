package org.moonwave.view.schedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.moonwave.jpa.bo.ScheduleBO;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.Schedule;
import org.moonwave.jpa.model.User;
import org.moonwave.util.DateUtil;
import org.moonwave.util.StringUtil;
import org.moonwave.view.BaseView;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Student event scheduler 
 * 
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class ScheduleView extends BaseView {


    private static final long serialVersionUID = 3781028814181168804L;
    static final Logger LOG = LoggerFactory.getLogger(ScheduleView.class);

    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();

    private List<User> students;
    private List<User> tutors;
    private String tutorId;
    private String studentId;
    private boolean tutorSetup = false;
    private boolean edit = false;
    private boolean remove = false;

    @PostConstruct
    public void init() {
        Map<String, String> rqm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        tutorSetup = (rqm.get("tutorSetup") != null && rqm.get("tutorSetup").equals("true"));
        edit = (rqm.get("edit") != null && rqm.get("edit").equals("true"));

        tutors = new UserBO().findAllTutors();
        students = new UserBO().findAllStudents();

        eventModel = new DefaultScheduleModel();
        List<Schedule> schedules = new ScheduleBO().getAllRoles();
        for (Schedule s : schedules) {
            eventModel.addEvent(scheduleToEvent(s));
        }
//        eventModel.addEvent(new DefaultScheduleEvent("Champions - today", previousDay8Pm(), previousDay11Pm(), "fc-foday"));
//        eventModel.addEvent(new DefaultScheduleEvent("Birthday - filled", today1Pm(), today6Pm(), "filled"));
//        eventModel.addEvent(new DefaultScheduleEvent("Breakfast - available", nextDay9Am(), nextDay11Am(), "available"));
//        eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));

    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutor) {
        this.tutorId = tutor;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String student) {
        this.studentId = student;
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

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public boolean isTutorSetup() {
        return tutorSetup;
    }

    public void setTutorSetup(boolean tutorSetup) {
        this.tutorSetup = tutorSetup;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Select an existing schedule event
     *
     * @param selectEvent
     */
    public void onEventSelect(SelectEvent selectEvent) {
        LOG.info("onEventSelect called");
        event = (ScheduleEvent) selectEvent.getObject();
        Schedule s = (Schedule)((DefaultScheduleEvent)event).getData();
        if (s.getTutorEvent()) { // for student, create a new event based on selected tutor event
            Schedule snew = new Schedule(s);
            snew.setId(null);
//            snew.setUserId(getLoggedInUser().getId());
            snew.setUser(null);
            snew.setAllDayEvent(false);
            snew.setTutorEvent(false);
            snew.setParentEventId(s.getId());
            event = scheduleToEvent(snew);
            this.studentId = (snew.getUserId() != null) ? String.valueOf(snew.getUserId()) : null;
            this.tutorId = (snew.getTutorId() != null) ? String.valueOf(snew.getTutorId()) : null;
        } else {
            this.studentId = (s.getUserId() != null) ? String.valueOf(s.getUserId()) : null;
            this.tutorId = (s.getTutorId() != null) ? String.valueOf(s.getTutorId()) : null;
        }
        DefaultScheduleEvent ev = (DefaultScheduleEvent)event;
        if (s.getUserId() == null) {
            this.edit = true; // new event, allow save
        } else {
            this.edit = getLoggedInUser().getId().equals(s.getUserId()); // a user can only edit his own event
        }
        this.remove = getLoggedInUser().getId().equals(s.getUserId()); // a user can only remove his own event
        ev.setEditable(this.edit);
    }

    /**
     * Select a date that does not have an event associated with it.
     * This allows a user to create a new event
     * 
     * @param selectEvent
     */
    public void onDateSelect(SelectEvent selectEvent) {
        LOG.info("onDateSelect called, " + (Date) selectEvent.getObject());
        resetFields();
        Calendar calendar = DateUtil.dateToCalendar((Date) selectEvent.getObject());
        calendar.add(Calendar.HOUR, 1);
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), calendar.getTime());
    }

    public void onViewChange(SelectEvent selectEvent) {
        LOG.info("onViewChange called");
        Object obj = selectEvent.getObject();
    }

    public void onTodaySelect(SelectEvent selectEvent) {
        LOG.info("onTodaySelect called");
        Object obj = selectEvent.getObject();
    }

    /**
     * Add a new event or update existing event
     *
     * @param actionEvent
     */
    public void saveEvent(ActionEvent actionEvent) {
        if (!validate(event)) {
            return;
        }
        if (event.getId() == null) {
            // date range validation
            Schedule s = this.packageData(null, event);
            super.getBasebo().persist(s);
            eventModel.addEvent(this.scheduleToEvent(s));
            super.info("Data saved successfully");
        }
        else {
            // date range validation
            Schedule data = (Schedule)((DefaultScheduleEvent)event).getData();
            Schedule s = this.packageData(data, event);
            super.getBasebo().update(s);
            eventModel.updateEvent(event);
            super.info("Data saved successfully");
        }
        event = new DefaultScheduleEvent();
    }

    /**
     * Remove an existing event
     *
     * @param actionEvent
     */
    public void removeEvent(ActionEvent actionEvent) {
        // check whether an event can be removed
        // student cannot remove tutor's event
        if (event.getId() != null) {
            Object data = ((DefaultScheduleEvent)event).getData();
            super.getBasebo().remove((Schedule) data);
            eventModel.deleteEvent(event);
            super.info("Data remove successfully");
            resetFields();
        }
        event = new DefaultScheduleEvent();
    }

    // ========================================================= Private methods
    private boolean validate(ScheduleEvent event) {
        boolean ret = true;
        if (StringUtil.nullOrEmpty(tutorId)) {
            super.error("Tutor is empty");
            ret = false;
        }
        return ret;
    }

    private Schedule packageData(Schedule s, ScheduleEvent event) {
        if (s == null) {
            s = (Schedule)event.getData();
            s.setCreateTime(super.getSqlTimestamp());
        }
        s.setTutorId(StringUtil.nullOrEmpty(tutorId) ? null : Integer.parseInt(tutorId));
        s.setUserId(StringUtil.nullOrEmpty(studentId) ? null : Integer.parseInt(studentId));
        s.setEvent(event.getTitle());
        s.setAllDayEvent(event.isAllDay());
        s.setStartTime(event.getStartDate());
        s.setEndTime(event.getEndDate());
        return s;
    }

    private DefaultScheduleEvent scheduleToEvent(Schedule s) {
        DefaultScheduleEvent e = new DefaultScheduleEvent();
        e.setData(s);
        e.setAllDay(s.getAllDayEvent());
        e.setEditable(true);
        if (s.getTutorEvent()) {
            e.setEditable(false); // tutor event not editable for student
            e.setStyleClass("tutorEvent");
        } else if (s.getParentEventId() != null) {
            if (!super.getLoggedInUser().getId().equals(s.getUserId())) {
                e.setEditable(false); // a student cannot edit another student's event
                e.setStyleClass("filled");
            } else {
                e.setStyleClass("available");
            }
        }
        e.setTitle(s.getEvent());
        e.setStartDate(s.getStartTime());
        e.setEndDate(s.getEndTime());
        return e;
    }

    private void resetFields() {
        this.studentId = null;
        this.tutorId = null;
        this.remove = false;
    }
}
