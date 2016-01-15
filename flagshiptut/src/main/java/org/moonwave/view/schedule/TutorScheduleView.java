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
 * Tutor event scheduler 
 * 
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class TutorScheduleView extends BaseView {

    private static final long serialVersionUID = -7360800471124663579L;
    static final Logger LOG = LoggerFactory.getLogger(TutorScheduleView.class);

    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();

    private List<User> students;
    private List<User> tutors;
    private String tutorId;
    private String studentId;
    private boolean tutorSetup = false;
    private boolean edit = false;
    private boolean remove = false;

    private int allDayStartHour = Integer.valueOf(AppProperties.getInstance().getProperty(AppProperties.KEY_all_day_start));
    private int allDayEndHour = Integer.valueOf(AppProperties.getInstance().getProperty(AppProperties.KEY_all_day_end));

    @PostConstruct
    public void init() {
        Map<String, String> rqm = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        tutorSetup = true;
        tutorId = super.getLoggedInUser().getId().toString();
        edit = (rqm.get("edit") != null && rqm.get("edit").equals("true"));

        tutors = new UserBO().findAllTutors();
        students = new UserBO().findAllStudents();

        eventModel = new DefaultScheduleModel();

        List<Schedule> schedules = new ScheduleBO().getAllRoles();
        for (Schedule s : schedules) {
            eventModel.addEvent(scheduleToEvent(s));
        }

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
        Schedule data = (Schedule)((DefaultScheduleEvent)event).getData();
        this.studentId = (data.getUserId() != null) ? String.valueOf(data.getUserId()) : null;
        this.tutorId = (data.getTutorId() != null) ? String.valueOf(data.getTutorId()) : null;
        this.remove = true;
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

    // ========================================================= Action Listener

    /**
     * Add a new event or update existing event
     *
     * @param actionEvent
     */
    public void saveEvent(ActionEvent actionEvent) {
        if (!validate(event)) {
            return;
        }
        if (this.event.isAllDay()) {
            DefaultScheduleEvent ev = (DefaultScheduleEvent) event;
            Date startDate = DateUtil.setHour(ev.getStartDate(), allDayStartHour);
            Date endDate = DateUtil.setHour(ev.getEndDate(), allDayEndHour);
            ev.setStartDate(startDate);
            ev.setEndDate(endDate);
            ev.setAllDay(false); // this allows data be displayed in the hours section
        }
        if (event.getId() == null) {
            // date range validation
            Schedule s = this.packageData(null, event);
            eventModel.addEvent(scheduleToEvent(s));
            s.setTutorEvent(true);
            super.getBasebo().persist(s);
            super.info("Data saved successfully");
        } else {
            // date range validation
            eventModel.updateEvent(event);
            Object data = ((DefaultScheduleEvent)event).getData();
            Schedule s = this.packageData((Schedule) data, event);
            super.getBasebo().update(s);
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
        // tutor cannot remove another tutor's event
        if (event.getId() != null) {
            Schedule schedule = (Schedule) ((DefaultScheduleEvent)event).getData();
            if (!getLoggedInUser().getId().equals(schedule.getTutorId())) {
                error("You can not delete another tutor's event");
                return;
            }
            super.getBasebo().remove(schedule);
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
            s = new Schedule();
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

    private ScheduleEvent scheduleToEvent(Schedule s) {
        DefaultScheduleEvent e = new DefaultScheduleEvent();
        e.setData(s);
        e.setAllDay(s.getAllDayEvent());
        e.setEditable(true);
        e.setTitle(s.getEvent());
        e.setStartDate(s.getStartTime());
        e.setEndDate(s.getEndTime());
        if (s.getTutorEvent()) {
            if (!super.getLoggedInUser().getId().equals(s.getTutorId())) {
                e.setEditable(false); // cannot edit another tutor's event
                e.setStyleClass("filled");
            } else {
                e.setStyleClass("available");
            }
        }
        return e;
    }

    private void resetFields() {
        this.studentId = null;
        this.tutorId = null;
        this.remove = false;
    }
}
