package org.moonwave.view.content.view;

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
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.moonwave.view.file.FileDownloadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Announcements View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class AnnouncementsView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(AnnouncementsView.class);

    private List<Announcement> data;
    private Announcement current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @ManagedProperty("#{fileDownloadView}")
    private FileDownloadView fileDownloadView;

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

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
            this.current = bo.findById(Integer.parseInt(selectedId));
            fileDownloadView.loadDownloads4Announcement(current.getUser().getId(), current.getId());
        }
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

    public String hasAttachments(Announcement a) {
        List<Upload> list = new UploadBO().findByUserAnnouncement(a.getUser().getId(), a.getId());
        return list.isEmpty() ? "No" : "Yes";
    }

    // ========================================================= Private methods

    /**
     * only show published announcements in default date range, ordered by latest
     * to oldest
     */
    private List <Announcement> loadAndFilterData() {
        GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
        List <Announcement> list = bo.findAllInDateRange(); 
        List <Announcement> ret = new ArrayList<Announcement>();
        for (Announcement a : list) {
            if (a.getPublished()) {
                ret.add(a);
            }
        }
        return ret;
    }
}