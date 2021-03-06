package org.moonwave.view.schedule;

import java.util.ArrayList;
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
import org.moonwave.util.AppProperties;
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
    private DefaultScheduleEvent event = new DefaultScheduleEvent();

    private List<User> students = new ArrayList<>();
    private List<User> tutors;
    private String tutorId;
    private String studentId;
    private boolean tutorSetup = false;

    // helper fields
    private boolean allowSave = false;
    private boolean allowRemove = false;
    String eventTitle;
    boolean showTutor = false;
    boolean showDates = true;

    @PostConstruct
    public void init() {
        Map<String, String> rqm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        tutorSetup = (rqm.get("tutorSetup") != null && rqm.get("tutorSetup").equals("true"));
        allowSave = (rqm.get("edit") != null && rqm.get("edit").equals("true"));

        tutors = new UserBO().findAllTutors();
        students.add(new UserBO().findById(super.getLoggedInUser().getId()));

        eventModel = new DefaultScheduleModel();
        List<Schedule> schedules = new ScheduleBO().getAllRoles();
        for (Schedule s : schedules) {
            eventModel.addEvent(scheduleToEvent(s));
        }
        studentId =  super.getLoggedInUser().getId().toString();
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

    public DefaultScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(DefaultScheduleEvent event) {
        this.event = (DefaultScheduleEvent)event;
    }

    public boolean isTutorSetup() {
        return tutorSetup;
    }

    public void setTutorSetup(boolean tutorSetup) {
        this.tutorSetup = tutorSetup;
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
        event = (DefaultScheduleEvent) selectEvent.getObject();
        eventTitle = super.getLocaleLabels().getString("eventDetails");
        showTutor = true;
        showDates = !event.isAllDay();
        Schedule s = (Schedule)((DefaultScheduleEvent)event).getData();
        if (s.isTutorEvent()) { // for student, create a new event based on selected tutor event
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
            this.allowSave = true; // new event, allow save
        } else {
            this.allowSave = getLoggedInUser().getId().equals(s.getUserId()); // a user can only edit his own event
        }
        this.allowRemove = getLoggedInUser().getId().equals(s.getUserId()); // a user can only remove his own event
        showTutor = s.isTutorEvent() || (this.tutorId != null);
        if (s.isTutorEvent()) {
            eventTitle = super.getLocaleLabels().getString("tutorEvent");
        }
        if (!showTutor) {
            eventTitle = super.getLocaleLabels().getString("personalEvent");
        }
        ev.setEditable(this.allowSave);
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
        eventTitle = super.getLocaleLabels().getString("createPersonalEvent");
        showTutor = false;
        Calendar calendar = DateUtil.dateToCalendar((Date) selectEvent.getObject());
        calendar.add(Calendar.HOUR, 1);
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), calendar.getTime());
        this.allowSave = true; // new event, allow save
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
        if (event.getId() == null) {
            // date range validation
            Schedule s = this.packageData(null, event);
            if (!validate(s)) {
                return;
            }
            super.getBasebo().persist(s);
            eventModel.addEvent(this.scheduleToEvent(s));
            super.info("Data saved successfully");
        }
        else {
            // date range validation
            Schedule data = (Schedule)((DefaultScheduleEvent)event).getData();
            Schedule s = this.packageData(data, event);
            if (!validate(s)) {
                return;
            }
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

    // ========================================================= Action Listener

    public void onAllDayChanged() {
        this.showDates = !this.event.isAllDay();
    }

    // ========================================================= Private methods

    /**
     * Return true if pass validation, false otherwise
     */
    private boolean validate(Schedule s) {
        boolean ret = true;
        if (s.getStartTime() == null) {
            super.error("Start date is empty");
            ret = false;
        }
        if (s.getEndTime() == null) {
            super.error("End date is empty");
            ret = false;
        }
        if (s.getStartTime().after(s.getEndTime())) {
            super.error("Start time is after End time");
            ret = false;
        }
        // check start time and end time range against other student's schedules
        List<Schedule> list1 = new ArrayList<Schedule>();
        if (s.getTutorId() != null) {
            list1.addAll(new ScheduleBO().findByTutorIdDate(s.getTutorId(), s.getStartTime()));
        }
        boolean duplicate = false;
        for (Schedule item : list1) {
            if (item.isTutorEvent()) {
                continue;
            }
            if (isInDateRange(item.getStartTime(), item.getEndTime(), s.getStartTime()) || isInDateRange(item.getStartTime(), item.getEndTime(), s.getEndTime())) {
                if (s.getId() == null) {
                    duplicate = true;
                    break;
                } else if (!s.getId().equals(item.getId())) {
                    duplicate = true;
                    break;
                }
            }
        }
        if (duplicate) {
            super.error("Event start time / end time conflicts with existing events");
            ret = false;
        }

        // check start time and end time range against own schedules
        List<Schedule> list2 = new ArrayList<Schedule>();
        if (s.getUserId() != null) {
            list2.addAll(new ScheduleBO().findByUserIdDate(s.getUserId(), s.getStartTime()));
        }
        duplicate = false;
        for (Schedule item : list2) {
            if (isInDateRange(item.getStartTime(), item.getEndTime(), s.getStartTime()) || isInDateRange(item.getStartTime(), item.getEndTime(), s.getEndTime())) {
                if (s.getId() == null) {
                    duplicate = true;
                    break;
                } else if (!s.getId().equals(item.getId())) {
                    duplicate = true;
                    break;
                }
            }
        }
        if (duplicate) {
            super.error("Event start time / end time conflicts with existing events");
            ret = false;
        }
        if (s.getId() == null) { // new event 
            for (Schedule item : list1) {
                
            }
        }
        return ret;
    }

    private Schedule packageData(Schedule s, ScheduleEvent event) {
        if (s == null) {
            s = (Schedule)event.getData();
            if (s == null) {
                s = new Schedule();
            }
            s.setCreateTime(super.getSqlTimestamp());
        }
        s.setTutorId(StringUtil.nullOrEmpty(tutorId) ? null : Integer.parseInt(tutorId));
        s.setUserId(StringUtil.nullOrEmpty(studentId) ? null : Integer.parseInt(studentId));
        s.setEvent(event.getTitle());
        s.setAllDayEvent(event.isAllDay());
        s.setStartTime(event.getStartDate());
        s.setEndTime(event.getEndDate());
        if (event.isAllDay()) {
            s.setStartTime(DateUtil.setHour(s.getStartTime(), Integer.valueOf(AppProperties.getInstance().getProperty(AppProperties.KEY_all_day_start))));
            s.setEndTime(DateUtil.setHour(s.getEndTime(), Integer.valueOf(AppProperties.getInstance().getProperty(AppProperties.KEY_all_day_end))));
        }
        return s;
    }

    private DefaultScheduleEvent scheduleToEvent(Schedule s) {
        DefaultScheduleEvent e = new DefaultScheduleEvent();
        e.setData(s);
        e.setAllDay(s.getAllDayEvent());
        e.setEditable(true);
        if (s.isTutorEvent()) {
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
        this.tutorId = null;
        this.allowRemove = false;
        this.showDates = true;
    }

    // ========================================================== Helper Methods

    public String getEventTitle() {
        return this.eventTitle;
    }

    public boolean showTutor() {
        return this.showTutor;
    }

    public boolean showDates() {
        return this.showDates;
    }

    public boolean allowSave() {
        return allowSave; // true - allow save
    }

    public boolean allowRemove() {
        return allowRemove; // true - allow remove
    }
    
    /**
     * Return true if dateToCheck falls into start date and end date range
     * 
     */
    private boolean isInDateRange(Date startDate, Date endDate, Date dateToCheck) {
        boolean ret = false;
        if (startDate.before(dateToCheck) && dateToCheck.before(endDate)) {
            ret = true;
        }
        return ret;
    }
}
