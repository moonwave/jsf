package org.moonwave.view.content.add;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.bo.UploadBO;
import org.moonwave.jpa.model.Announcement;
import org.moonwave.jpa.model.Upload;
import org.moonwave.util.StringUtil;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.moonwave.view.file.FileDownloadView;
import org.moonwave.view.file.FileUploadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Announcement List View
 * 
 * Modified from AnnouncementsView
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class AnnouncementListView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(AnnouncementListView.class);

    private List<Announcement> data;
    private Announcement current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @ManagedProperty("#{fileDownloadView}")
    private FileDownloadView fileDownloadView;

    @ManagedProperty("#{fileUploadView}")
    private FileUploadView fileUploadView;

    @PostConstruct
    public void init() {
        data = loadAndFilterData();
    }

    public List<Announcement> getData() {
        return data;
    }

    public void setData(List<Announcement> data) {
        this.data = data;
    }

    public Announcement getCurrent() {
        return current;
    }

    public void setCurrent(Announcement current) {
        this.current = current;
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    public FileDownloadView getFileDownloadView() {
        return fileDownloadView;
    }

    public void setFileDownloadView(FileDownloadView fileDownloadView) {
        this.fileDownloadView = fileDownloadView;
    }

    public FileUploadView getFileUploadView() {
        return fileUploadView;
    }

    public void setFileUploadView(FileUploadView fileUploadView) {
        this.fileUploadView = fileUploadView;
    }

    public String hasAttachments(Announcement a) {
        List<Upload> list = new UploadBO().findByUserAnnouncement(a.getUser().getId(), a.getId());
        return list.isEmpty() ? "No" : "Yes";
    }

    // ======================================================= Property Listener

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            fileDownloadView.setDownloads(new UploadBO().findByUserAnnouncement(current.getUser().getId(), current.getId()));
        }
    }

    // ========================================================== ActionListener

    public void newAnnouncement() throws IOException {
        super.redirectTo("/content/add/announcement.xhtml");
    }

    /**
     *
    * @param selectedId selected Announcement id
     */
    public void togglePublish(String selectedId) {
        if (!StringUtil.nullOrEmpty(selectedId)) {
            GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            this.current.setPublished(!this.current.getPublished());
            super.getBasebo().update(current);

            data = loadAndFilterData();
        }
    }

    /**
    *
    * @param selectedId selected Announcement id
    */
    public void edit(String selectedId) throws IOException {
        super.redirectTo("/content/add/announcement.xhtml?selectedId=" + selectedId);
    }


    public void delete(String selectedId) throws IOException {
        try {
            GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            fileUploadView.loadUploads4Announcement(current.getUser().getId(), current.getId());
            fileUploadView.deleteAllFromDB();

            this.fileUploadView.clearFields();
            super.getBasebo().remove(current);

            // show successful message and reset fields
            super.info("Data deletion successful");

        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
        super.redirectTo("/content/add/announcementList.xhtml");
    }

    // ========================================================= Private methods

    /**
     * For tutor, teacher, only show their own announcements
     * For supervisor, show all announcements
     */
    private List <Announcement> loadAndFilterData() {
        GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
        List <Announcement> list = bo.findAllInDateRange(); 
        List <Announcement> ret = new ArrayList<Announcement>();
        boolean isSupervisor = accessController.isSupervisor();
        boolean isTutorOrTeacher = accessController.isTutor() || accessController.isTeacher();
        for (Announcement a : list) {
            if (isSupervisor) {
                ret.add(a);
            } else if (isTutorOrTeacher) {
                if (accessController.isSelf(a.getUser())) {
                    ret.add(a);
                }
            }
        }
        return ret;
    }
}